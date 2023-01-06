package swp.happyprogramming.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "mentor")
@Getter
@Setter
@ToString
public class Mentor {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "created")
    private Date created;
    @Column(name = "modified")
    private Date modified;


    public Mentor() {
        this.created = Date.from(Instant.now());
        this.modified = Date.from(Instant.now());
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_skills",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Collection<Skill> skills;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "mentor_experience",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "experience_id"))
    private  Collection<Experience> experiences;

}
