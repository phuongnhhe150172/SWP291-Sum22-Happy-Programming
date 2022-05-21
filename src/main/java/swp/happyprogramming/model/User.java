package swp.happyprogramming.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
<<<<<<< HEAD:src/main/java/swp391_sum22/happyprogramming/model/User.java
import java.util.Collection;
=======
import java.time.Instant;
>>>>>>> Develop:src/main/java/swp/happyprogramming/model/User.java
import java.util.Date;


@Entity
<<<<<<< HEAD:src/main/java/swp391_sum22/happyprogramming/model/User.java
@Table(name = "users")
@Data
=======
@Table(name = "Users")
@Getter
@Setter
@ToString
>>>>>>> Develop:src/main/java/swp/happyprogramming/model/User.java
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

<<<<<<< HEAD:src/main/java/swp391_sum22/happyprogramming/model/User.java
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;
=======
    public User() {
        this.created = Date.from(Instant.now());
        this.modified = Date.from(Instant.now());
    }

>>>>>>> Develop:src/main/java/swp/happyprogramming/model/User.java

}
