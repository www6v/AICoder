

// Q: 实现一个电影票购票功能
// 需求：
// 1. 需要支持选座位（可以一次购买多个座位）
// 2. 电影票需要包含，电影名称，放映厅，播放日期时间，以及座位信息
// 3. 请支持多人同时购票
// 4. 使用DDD的理念来实现以上功能 ，需要有VO，PO， Repository， Service， Biz  
// Value Object


public class MovieTicket {
    private String movieName;
    private String hall;
    private LocalDateTime showTime;
    private List<String> seats;

    // getters and setters
}

// Repository
public interface MovieTicketRepository {
    void save(MovieTicket movieTicket);
}

// Service
public class MovieTicketService {
    private MovieTicketRepository movieTicketRepository;

    public void purchaseTicket(MovieTicket movieTicket) {
        // logic to handle multiple users purchasing tickets simultaneously
        synchronized(this) {
            movieTicketRepository.save(movieTicket);
        }
    }
}













    

