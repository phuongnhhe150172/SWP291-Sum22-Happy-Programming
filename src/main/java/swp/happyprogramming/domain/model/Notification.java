package swp.happyprogramming.domain.model;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class Notification {

  private Long id = 0L;

  private String content;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date created;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date modified;

  @JoinTable(
    name = "role_has_notification",
    joinColumns = @JoinColumn(
      name = "noti_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(
      name = "role_id", referencedColumnName = "id"))
  private Collection<Role> notificationToRoles;


  public Notification() {
    created = Date.from(Instant.now());
    modified = Date.from(Instant.now());
  }

}
