package swp391_sum22.happyprogramming.model;

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
    private long user_id;
    @Column(name = "dob")
    private Date dob;
    @Column(name = "phone_number")
    private String phone_number;
    @Column(name = "address_id")
    private int address_id;
    @Column(name = "bio")
    private String bio;
    @Column(name = "school")
    private String school;
    @Column(name = "method_id")
    private int method_id;
    @Column(name = "price")
    private float price;
    @Column(name = "created")
    private Date created;
    @Column(name = "modified")
    private Date modified;
}
