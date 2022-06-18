package swp.happyprogramming.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "province")
@Getter
@Setter
public class Province {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    public Province() {
        this.id = 1;
    }

    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
    private Collection<District> districts;
}
