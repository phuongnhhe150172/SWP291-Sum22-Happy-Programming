package swp.happyprogramming.model;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorExperienceId implements Serializable {
    private long mentorId;

    private long experienceId;
}
