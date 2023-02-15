package swp.happyprogramming.domain.model;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "method")
@Data
public class Method {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "method")
    private Collection<Post> posts;
}
