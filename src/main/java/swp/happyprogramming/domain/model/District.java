package swp.happyprogramming.domain.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "district")
@Getter
@Setter
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    public District() {
        this.id = 1;
    }

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
    private Collection<Ward> wards;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "province_id")
    private Province province;
}
