package swp.happyprogramming.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class User {

  private Long id;

  private String firstName;
  private String lastName;

  private String email;
  private String password;

  private Integer gender;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date dob;
  private String phoneNumber;
  private String bio;
  private String school;
  private Integer isOnline;
  private Integer isOffline;
  private Double price;
  private String image = "/upload/static/imgs/avatar_default.jpg";
  private Integer status = 1;

  private Date created;

  private Date modified;

  private String resetPasswordToken;

  private Collection<Role> roles;
  private Address address;
  private Collection<Mentor> mentors;
  private Collection<Post> posts;
  private Collection<Connect> connects1;
  private Collection<Connect> connects2;
  private Collection<Request> requestReceived;
  private Collection<Request> requestSent;

  public User() {
    this.created = Date.from(Instant.now());
    this.modified = Date.from(Instant.now());
    this.roles = new ArrayList<>();
  }

  public String getResetPasswordToken() {
    return resetPasswordToken;
  }

  public void setResetPasswordToken(String resetPasswordToken) {
    this.resetPasswordToken = resetPasswordToken;
  }

  public void addRole(Role role) {
    this.roles.add(role);
  }

  public void markModified() {
    this.modified = Date.from(Instant.now());
  }

}