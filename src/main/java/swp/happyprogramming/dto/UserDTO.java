package swp.happyprogramming.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.validator.auth.PasswordMatches;
import swp.happyprogramming.validator.auth.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@PasswordMatches(message = "Passwords must match")
public class UserDTO {
    private long id;
    private long profileId;
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private int gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    private String phoneNumber;
    private long addressId;
    private String ward;
    private String district;
    private String province;
    private String street;
    private String bio;
    private String school;
    private float price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modified;

//    @NotNull
//    @NotEmpty
//    private String password;
//    private String matchingPassword;

//    @ValidEmail
//    @NotNull
//    @NotEmpty
//    private String email;
    @NotNull
    private int role;
    public UserDTO() {
        this.created = Date.from(Instant.now());
        this.modified = Date.from(Instant.now());
    }
}
