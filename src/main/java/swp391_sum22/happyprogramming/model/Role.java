package swp391_sum22.happyprogramming.model;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created")
    private Date created;
    @Column(name = "modified")
    private Date modified;

    public Role (String name){
        this.name = name;
    }

    public Role() {

    }
}
