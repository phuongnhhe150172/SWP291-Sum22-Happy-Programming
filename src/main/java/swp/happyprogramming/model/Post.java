package swp.happyprogramming.model;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "posts")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "mentee_id")
    private long menteeId;

    @Column(name = "description")
    private String description;
    
    @Column(name = "method_id")
    private long methodId;

    @Column(name = "price")
    private float price;

    @Column(name = "created")
    private Date created;

    @Column(name = "modified")
    private Date modified;

    @Column(name = "status")
    private int status;

    public Post(){
        created = Date.from(Instant.now());
        modified = Date.from(Instant.now());
    }
}