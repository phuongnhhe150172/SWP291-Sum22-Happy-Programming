package swp.happyprogramming.domain.model;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class District {

  private long id;

  private String name;

  private String type;
  private Collection<Ward> wards;
  private Province province;

  public District() {
    this.id = 1;
  }
}
