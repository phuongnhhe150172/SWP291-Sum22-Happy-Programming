package swp.happyprogramming.domain.model;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Province {

  private long id;

  private String name;

  private String type;
  private Collection<District> districts;

  public Province() {
    this.id = 1;
  }
}
