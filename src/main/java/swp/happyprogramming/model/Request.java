package swp.happyprogramming.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "request")
@Data
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "mentor_id")
    private long mentorId;

    @Column(name = "mentee_id")
    private long menteeId;

    @Column(name = "status")
    private int status;

    @Column(name = "skill_id")
    private long skillId;

    @Column(name = "budget")
    private long budget;


}
