package swp.happyprogramming.dto;

import lombok.Data;


@Data
public class RequestDTO {
    private long id;

    private String mentorName;

    private String menteeName;

    private int status;

    private String skillName;

    private long budget;
}
