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

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<User> users ;
}
