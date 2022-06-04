package swp.happyprogramming.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    private int gender;
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
    private int isOnline;
    @Column(name = "is_offline")
    private int isOffline;
    @Column(name = "price")
    private float price;
    @Column(name = "address_id")
    private long addressId;

    @Column(name = "created")
    private Date created;
    @Column(name = "modified")
    private Date modified;

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

    public void setRoles(Role role) {
        this.roles.add(role);
    }
}
