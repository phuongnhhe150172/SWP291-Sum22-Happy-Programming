package swp.happyprogramming.domain.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

  private Long id;

  private User sender;

  private User receiver;

  private String content;

  private Instant timestamp;

  private String title;

  private String image;

  private String link;
}
