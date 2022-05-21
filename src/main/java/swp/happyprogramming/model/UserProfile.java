package swp.happyprogramming.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@ToString
public class UserProfile {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_id")
    private long userID;
    @Column(name = "gender")
    private int gender;
    @Column(name = "dob")
    private Date dob;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "bio")
    private String bio;
    @Column(name = "school")
    private String school;
    @Column(name = "method_id")
    private int methodID;
    @Column(name = "price")
    private float price;
    @Column(name = "created")
    private Date created;
    @Column(name = "modified")
    private Date modified;

    public UserProfile() {
        this.created = Date.from(Instant.now());
        this.modified = Date.from(Instant.now());
    }
}
