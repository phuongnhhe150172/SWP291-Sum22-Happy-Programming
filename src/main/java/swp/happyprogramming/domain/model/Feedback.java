package swp.happyprogramming.domain.model;

import java.time.Instant;
import lombok.Data;

@Data
public class Feedback {

  private long id;

  private User receiver;

  private User sender;

  private int rate;

  private String comment;

  private Instant created;
}
