package swp.happyprogramming.domain.model;

import java.time.Instant;
import java.util.Date;
import lombok.Data;

@Data
public class Post {

  private long id;

  private String description;

  private float price;

  private Date created;

  private Date modified;

  private int status;
  private User user;
  private Method method;

  public Post() {
    created = Date.from(Instant.now());
    modified = Date.from(Instant.now());
  }
}