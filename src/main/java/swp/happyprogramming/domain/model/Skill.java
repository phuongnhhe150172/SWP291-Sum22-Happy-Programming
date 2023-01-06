package swp.happyprogramming.domain.model;

import lombok.*;

import jakarta.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "Skills")
@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "skills", cascade = CascadeType.ALL)
    private Collection<Mentor> mentors;
}
