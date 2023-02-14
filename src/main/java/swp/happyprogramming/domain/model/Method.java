package swp.happyprogramming.domain.model;

import java.util.Collection;
import lombok.Data;

@Data
public class Method {

  private long id;

  private String name;

  private Collection<Post> posts;
}
