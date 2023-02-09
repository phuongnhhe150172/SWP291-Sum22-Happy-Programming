package swp.happyprogramming.adapter.validator.auth;

import swp.happyprogramming.adapter.dto.UserDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {

    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        return (validateEmail(obj));
    }

    private boolean validateEmail(Object obj) {
        UserDTO user = (UserDTO) obj;
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(user.getPassword());
        return matcher.matches();
    }
}