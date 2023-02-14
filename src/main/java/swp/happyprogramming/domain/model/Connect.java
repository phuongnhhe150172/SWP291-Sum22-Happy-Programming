package swp.happyprogramming.domain.model;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Connect {

  private long id;

  private Instant created;
  private User user1;
  private User user2;

  public Connect() {
    this.created = Instant.now();
  }
}
