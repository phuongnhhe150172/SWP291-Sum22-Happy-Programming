package swp.happyprogramming.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "district")
@Getter
@Setter
@ToString
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

//    @Column(name = "province_id", insertable = false, updatable = false)
//    private long provinceId;

    public District() {
        this.id = 1;
    }

    @OneToMany(mappedBy = "district")
    private Collection<Ward> wards;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;
}
