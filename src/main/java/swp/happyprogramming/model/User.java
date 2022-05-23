package swp.happyprogramming.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
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

    @Column(name = "created")
    private Date created;
    @Column(name = "modified")
    private Date modified;

    private ArrayList<Role> roles;

    public User() {
        this.created = Date.from(Instant.now());
        this.modified = Date.from(Instant.now());
        this.roles = new ArrayList<>();
    }

    public void setRoles(Role role) {
        this.roles.add(role);
    }
}
