package swp.happyprogramming.model;

import lombok.*;

import javax.persistence.*;

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
}
