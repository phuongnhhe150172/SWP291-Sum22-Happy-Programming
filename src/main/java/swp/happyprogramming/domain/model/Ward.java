package swp.happyprogramming.domain.model;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ward {

  private long id;

  private String name;

  private String type;
  private Collection<Address> addresses;
  private District district;

  public Ward() {
    this.id = 1;
  }
}
