package swp391_sum22.happyprogramming.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swp391_sum22.happyprogramming.validator.auth.PasswordMatches;
import swp391_sum22.happyprogramming.validator.auth.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PasswordMatches
@ValidEmail
public class UserDTO {
    @NotNull
    @NotEmpty
    private String fullname;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;

    @NotNull
    @NotEmpty
    private String email;
}
