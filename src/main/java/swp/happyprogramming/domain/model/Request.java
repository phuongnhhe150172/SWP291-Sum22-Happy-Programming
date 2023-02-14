package swp.happyprogramming.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {

  private long id;

  private User mentor;

  private User mentee;
}
