package swp.happyprogramming.domain.model;

import java.time.Instant;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Care {

  private long userId;

  private long postId;

  private Date created;

  private Date modified;

  public Care() {
    created = Date.from(Instant.now());
    modified = Date.from(Instant.now());
  }

}


