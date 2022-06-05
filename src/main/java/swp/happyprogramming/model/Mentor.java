package swp.happyprogramming.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
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
    @Column(name = "user_id")
    private long userID;
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
}
