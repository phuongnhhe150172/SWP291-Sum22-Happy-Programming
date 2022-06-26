package swp.happyprogramming.dto;

import lombok.Data;


@Data
public class RequestDTO {
    private long id;

    private UserDTO mentor;

    private UserDTO mentee;
}
