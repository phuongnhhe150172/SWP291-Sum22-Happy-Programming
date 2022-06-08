package swp.happyprogramming.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.User;

public interface IUserService extends UserDetailsService {
    User registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException;
}
