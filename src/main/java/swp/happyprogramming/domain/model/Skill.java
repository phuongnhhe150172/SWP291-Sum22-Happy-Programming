package swp.happyprogramming.domain.model;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Skill {

  private Long id;

  @NonNull
  private String name;

  private Collection<Mentor> mentors;
}
