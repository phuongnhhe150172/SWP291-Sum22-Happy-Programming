package swp.happyprogramming.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import swp.happyprogramming.domain.validator.auth.PasswordMatches;
import swp.happyprogramming.domain.validator.auth.ValidEmail;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@PasswordMatches(message = "Passwords must match")
public class UserDTO {
    private long id;
    private long profileId;
    private String firstName;
    private String lastName;
    private int gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    private String phoneNumber;
    private AddressDTO address;
    private String bio;
    private String school;
    private Double price;
    private String image;
    private Integer status;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date created;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modified;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;

    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    private int role;

    public UserDTO() {
        this.created = Date.from(Instant.now());
        this.modified = Date.from(Instant.now());
    }

}
