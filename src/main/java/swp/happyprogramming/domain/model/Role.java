package swp.happyprogramming.domain.model;

import java.util.Collection;
import lombok.Data;

@Data
public class Role {

  private int id;

  private String name;
  private Collection<Notification> notifications;

  public Role(int id) {
    this.id = id;
    if (id == 1) {
      this.name = "ROLE_MENTOR";
    } else {
      this.name = "ROLE_MENTEE";
    }
  }

  public Role() {
  }


  public Role(int id, String name) {
    this.id = id;
    this.name = name;
  }
}
