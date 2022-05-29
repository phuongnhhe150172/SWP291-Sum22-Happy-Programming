package swp.happyprogramming.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.validator.auth.PasswordMatches;
import swp.happyprogramming.validator.auth.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@AllArgsConstructor
@Getter
@Setter
@PasswordMatches(message = "Passwords must match")
public class UserDTO {
    @NotNull
    @NotEmpty
    private String firstName;
    @NotNull
    @NotEmpty
    private String lastName;

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

    private ArrayList<Skill> skills;

    public UserDTO() {
        skills = new ArrayList<>();
    }
}
