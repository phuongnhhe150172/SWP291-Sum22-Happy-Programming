package swp.happyprogramming.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

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
            this.name = "mentor";
        } else {
            this.name = "mentee";
        }
    }

    public Role() {

    }
}
