package swp.happyprogramming.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "ward")
@Getter
@Setter
public class Ward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    public Ward() {
        this.id = 1;
    }

    @OneToMany(mappedBy = "ward", cascade = CascadeType.ALL)
    private Collection<Address> addresses;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "district_id")
    private District district;
}
