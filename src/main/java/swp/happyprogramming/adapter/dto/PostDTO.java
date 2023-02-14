package swp.happyprogramming.adapter.dto;

import java.time.Instant;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import swp.happyprogramming.domain.model.Method;

@Getter
@Setter
@Data
@AllArgsConstructor
public class PostDTO {

  private long id;
  private UserDTO user;
  private Method method;
  private String description;
  private float price;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date created;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date modified;
  private int status;

  public PostDTO() {
    created = Date.from(Instant.now());
    modified = Date.from(Instant.now());
  }
}
