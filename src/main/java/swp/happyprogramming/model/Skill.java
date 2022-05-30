package swp.happyprogramming.model;

import lombok.*;

import javax.persistence.*;

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
}
