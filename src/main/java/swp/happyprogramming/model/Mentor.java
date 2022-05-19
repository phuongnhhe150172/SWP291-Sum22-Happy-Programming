package swp.happyprogramming.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Mentors")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Mentor {
    @Column(name = "id")
    @Id
    private long id;
    @Column(name = "gender")
    private int gender;
    @Column(name = "user_id")
    private long userID;
    @Column(name = "dob")
    private Date dob;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "address_id")
    private int addressID;
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
}
