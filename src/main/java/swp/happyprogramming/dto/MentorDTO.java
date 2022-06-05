package swp.happyprogramming.dto;

import lombok.Getter;
import lombok.Setter;
import swp.happyprogramming.model.Experience;
import swp.happyprogramming.model.Skill;

import java.util.ArrayList;

@Getter
@Setter
public class MentorDTO extends UserDTO {
    private ArrayList<Skill> skills;
    private ArrayList<Experience> experiences;

    public MentorDTO() {
        super();
        this.skills = new ArrayList<>();
        this.experiences = new ArrayList<>();
    }
}
