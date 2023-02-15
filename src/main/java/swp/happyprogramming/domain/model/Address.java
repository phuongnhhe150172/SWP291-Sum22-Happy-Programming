package swp.happyprogramming.domain.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "Address")
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    public Address() {
        this.name = "";
    }

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "ward_id")
    private Ward ward;
}