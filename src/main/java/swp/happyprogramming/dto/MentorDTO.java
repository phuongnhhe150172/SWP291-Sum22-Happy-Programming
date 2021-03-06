package swp.happyprogramming.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import swp.happyprogramming.model.Experience;
import swp.happyprogramming.model.Skill;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MentorDTO extends UserDTO {
    private List<Skill> skills;
    private List<Experience> experiences;

    public MentorDTO() {
        super();
        this.skills = new ArrayList<>();
        this.experiences = new ArrayList<>();
    }
}
