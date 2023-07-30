# kubectl 获取所有pods， 按照restart count 排序
# 1. 获取所有 pods


kubectl get pods --all-namespaces -o jsonpath="{range .items[*]}{'\n'}{.metadata.name}{':\t'}{.status.containerStatuses[*].restartCount}{end}" | sort -k2 -n -r