package swp.happyprogramming.adapter.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.Date;

@Data
public class ConnectDTO {
    private long id;

    private UserDTO user1;

    private UserDTO user2;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created;

    public ConnectDTO(){
        this.created = Date.from(Instant.now());
    }
}
