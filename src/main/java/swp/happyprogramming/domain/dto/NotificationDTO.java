package swp.happyprogramming.domain.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;

    private String content;

    private String time;
}
