package swp391_sum22.happyprogramming.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "Users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "fullname")
    private String fullName;

    @Column(name = "email",unique = true)
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name = "created")
    private Date created;
    @Column(name = "modified")
    private Date modified;
    @Column(name = "profile_id")
    private Long profile_id;
}
