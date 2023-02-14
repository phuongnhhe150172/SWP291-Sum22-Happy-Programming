package swp.happyprogramming.domain.model;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Mentor {

  private long id;
  private Date created;
  private Date modified;
  private User user;

  public Mentor() {
    this.created = Date.from(Instant.now());
    this.modified = Date.from(Instant.now());
  }

  private Collection<Skill> skills;

  private Collection<Experience> experiences;
}
