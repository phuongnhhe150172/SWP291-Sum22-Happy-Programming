package swp.happyprogramming.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "Address")
@Getter
@Setter
@ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

//    @Column(name = "ward_id", insertable = false, updatable = false)
//    private long wardID;

    public Address() {
        this.name = "";
//        this.wardID = 1;
    }

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;
}
