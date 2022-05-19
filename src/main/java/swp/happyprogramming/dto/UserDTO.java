package swp.happyprogramming.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swp.happyprogramming.validator.auth.PasswordMatches;
import swp.happyprogramming.validator.auth.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PasswordMatches(message = "Passwords must match")
public class UserDTO {
    @NotNull
    @NotEmpty
    private String fullname;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;

    @ValidEmail(message = "Invalid email")
    @NotNull
    @NotEmpty
    private String email;
}
