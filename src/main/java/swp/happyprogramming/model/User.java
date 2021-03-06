package swp.happyprogramming.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    private Integer gender;
    @Column(name = "dob")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "bio")
    private String bio;
    @Column(name = "school")
    private String school;
    @Column(name = "is_online")
    private Integer isOnline;
    @Column(name = "is_offline")
    private Integer isOffline;
    @Column(name = "price")
    private Double price;
    @Column(name = "image")
    private String image;
    @Column(name = "status")
    private Integer status;

    @Column(name = "created")
    private Date created;
    @Column(name = "modified")
    private Date modified;

    @Column(name = "password_token")
    private String resetPasswordToken;

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public User() {
        this.created = Date.from(Instant.now());
        this.modified = Date.from(Instant.now());
        this.roles = new ArrayList<>();
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Mentor> mentors;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Post> posts;

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    private Collection<Connect> connects1;

    @OneToMany(mappedBy = "user2",cascade = CascadeType.ALL)
    private Collection<Connect> connects2;

    @OneToMany(mappedBy = "mentor",cascade = CascadeType.ALL)
    private Collection<Request> requestReceived;

    @OneToMany(mappedBy = "mentee",cascade = CascadeType.ALL)
    private Collection<Request> requestSent;
}