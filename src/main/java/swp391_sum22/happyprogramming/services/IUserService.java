package swp391_sum22.happyprogramming.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import swp391_sum22.happyprogramming.dto.UserDTO;
import swp391_sum22.happyprogramming.exception.auth.UserAlreadyExistException;
import swp391_sum22.happyprogramming.model.User;

public interface IUserService extends UserDetailsService {
    User registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException;
}
