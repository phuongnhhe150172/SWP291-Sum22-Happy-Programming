package swp.happyprogramming.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "experience")
@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NonNull
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "experiences", cascade = CascadeType.ALL)
    private Collection<Mentor> mentors;

}
