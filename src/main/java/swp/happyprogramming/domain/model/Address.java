package swp.happyprogramming.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

  private long id;

  private String name;
  private Ward ward;

  public Address() {
    this.name = "";
  }
}