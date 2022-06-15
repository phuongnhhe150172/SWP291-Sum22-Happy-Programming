package swp.happyprogramming.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swp.happyprogramming.model.Experience;
import swp.happyprogramming.model.Skill;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class MentorDTO extends UserDTO {
    private ArrayList<Skill> skills;
    private ArrayList<Experience> experiences;

    public MentorDTO() {
        super();
        this.skills = new ArrayList<>();
        this.experiences = new ArrayList<>();
    }
}
