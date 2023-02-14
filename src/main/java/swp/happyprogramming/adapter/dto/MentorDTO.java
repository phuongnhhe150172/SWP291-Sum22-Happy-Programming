package swp.happyprogramming.adapter.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import swp.happyprogramming.domain.model.Experience;
import swp.happyprogramming.domain.model.Skill;

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
