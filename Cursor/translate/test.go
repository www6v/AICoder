package translate

import (
	"fmt"
	"time"

	"github.com/armon/go-metrics"
)

// runFSM is a long running goroutine responsible for applying logs
// to the FSM. This is done async of other logs since we don't want
// the FSM to block our internal operations.
func (r *Raft) runFSM() {
	var lastIndex, lastTerm uint64

	batchingFSM, batchingEnabled := r.fsm.(BatchingFSM)
	configStore, configStoreEnabled := r.fsm.(ConfigurationStore)

	applySingle := func(req *commitTuple) {
		// Apply the log if a command or config change
		var resp interface{}
		// Make sure we send a response
		defer func() {
			// Invoke the future if given
			if req.future != nil {
				req.future.response = resp
				req.future.respond(nil)
			}
		}()

		switch req.log.Type {
		case LogCommand:
			start := time.Now()
			resp = r.fsm.Apply(req.log)
			metrics.MeasureSince([]string{"raft", "fsm", "apply"}, start)

		case LogConfiguration:
			if !configStoreEnabled {
				// Return early to avoid incrementing the index and term for
				// an unimplemented operation.
				return
			}

			start := time.Now()
			configStore.StoreConfiguration(req.log.Index, DecodeConfiguration(req.log.Data))
			metrics.MeasureSince([]string{"raft", "fsm", "store_config"}, start)
		}

		// Update the indexes
		lastIndex = req.log.Index
		lastTerm = req.log.Term
	}

	applyBatch := func(reqs []*commitTuple) {
		if !batchingEnabled {
			for _, ct := range reqs {
				applySingle(ct)
			}
			return
		}

		// Only send LogCommand and LogConfiguration log types. LogBarrier types
		// will not be sent to the FSM.
		shouldSend := func(l *Log) bool {
			switch l.Type {
			case LogCommand, LogConfiguration:
				return true
			}
			return false
		}

		var lastBatchIndex, lastBatchTerm uint64
		sendLogs := make([]*Log, 0, len(reqs))
		for _, req := range reqs {
			if shouldSend(req.log) {
				sendLogs = append(sendLogs, req.log)
			}
			lastBatchIndex = req.log.Index
			lastBatchTerm = req.log.Term
		}

		var responses []interface{}
		if len(sendLogs) > 0 {
			start := time.Now()
			responses = batchingFSM.ApplyBatch(sendLogs)
			metrics.MeasureSince([]string{"raft", "fsm", "applyBatch"}, start)
			metrics.AddSample([]string{"raft", "fsm", "applyBatchNum"}, float32(len(reqs)))

			// Ensure we get the expected responses
			if len(sendLogs) != len(responses) {
				panic("invalid number of responses")
			}
		}

		// Update the indexes
		lastIndex = lastBatchIndex
		lastTerm = lastBatchTerm

		var i int
		for _, req := range reqs {
			var resp interface{}
			// If the log was sent to the FSM, retrieve the response.
			if shouldSend(req.log) {
				resp = responses[i]
				i++
			}

			if req.future != nil {
				req.future.response = resp
				req.future.respond(nil)
			}
		}
	}

	restore := func(req *restoreFuture) {
		// Open the snapshot
		meta, source, err := r.snapshots.Open(req.ID)
		if err != nil {
			req.respond(fmt.Errorf("failed to open snapshot %v: %v", req.ID, err))
			return
		}
		defer source.Close()

		snapLogger := r.logger.With(
			"id", req.ID,
			"last-index", meta.Index,
			"last-term", meta.Term,
			"size-in-bytes", meta.Size,
		)

		// Attempt to restore
		if err := fsmRestoreAndMeasure(snapLogger, r.fsm, source, meta.Size); err != nil {
			req.respond(fmt.Errorf("failed to restore snapshot %v: %v", req.ID, err))
			return
		}

		// Update the last index and term
		lastIndex = meta.Index
		lastTerm = meta.Term
		req.respond(nil)
	}

	snapshot := func(req *reqSnapshotFuture) {
		// Is there something to snapshot?
		if lastIndex == 0 {
			req.respond(ErrNothingNewToSnapshot)
			return
		}

		// Start a snapshot
		start := time.Now()
		snap, err := r.fsm.Snapshot()
		metrics.MeasureSince([]string{"raft", "fsm", "snapshot"}, start)

		// Respond to the request
		req.index = lastIndex
		req.term = lastTerm
		req.snapshot = snap
		req.respond(err)
	}

	saturation := newSaturationMetric([]string{"raft", "thread", "fsm", "saturation"}, 1*time.Second)

	for {
		saturation.sleeping()

		select {
		case ptr := <-r.fsmMutateCh:
			saturation.working()

			switch req := ptr.(type) {
			case []*commitTuple:
				applyBatch(req)

			case *restoreFuture:
				restore(req)

			default:
				panic(fmt.Errorf("bad type passed to fsmMutateCh: %#v", ptr))
			}

		case req := <-r.fsmSnapshotCh:
			saturation.working()

			snapshot(req)

		case <-r.shutdownCh:
			return
		}
	}
}
