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
public class Experience {

  private long id;

  @NonNull
  private String description;

  private Collection<Mentor> mentors;

}
