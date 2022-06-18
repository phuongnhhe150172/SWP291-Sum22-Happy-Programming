package swp.happyprogramming.model;

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

    public Address() {
        this.name = "";
    }

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "ward_id")
    private Ward ward;
}