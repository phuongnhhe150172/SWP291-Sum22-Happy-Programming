package swp.happyprogramming.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "method")
@Data
public class Method {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;
}
