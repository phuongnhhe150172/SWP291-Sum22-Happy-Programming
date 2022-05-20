package swp.happyprogramming.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Skills")
@Data
public class Skill {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int skill_id;

    @Column(name = "name")
    private String skillName;

    @ManyToMany(mappedBy = "skillSet")
    @JsonBackReference()
    private Set<User> userSet;

}
