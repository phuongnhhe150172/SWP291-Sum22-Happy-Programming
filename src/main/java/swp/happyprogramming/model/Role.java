package swp.happyprogramming.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @Column(name = "id", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    public Role(int id) {
        this.id = id;
        if (id == 1) {
            this.name = "ROLE_MENTOR";
        } else {
            this.name = "ROLE_MENTEE";
        }
    }

    public Role() {
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"))
    private Collection<User> users ;
}
