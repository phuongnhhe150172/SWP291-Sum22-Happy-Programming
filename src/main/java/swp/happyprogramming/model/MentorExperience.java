package swp.happyprogramming.model;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "mentor_experience")
@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(MentorExperienceId.class)
public class MentorExperience {
    @Id
    @Column(name = "mentor_id")
    private long mentorId;

    @Id
    @Column(name = "experience_id")
    private long experienceId;
}
