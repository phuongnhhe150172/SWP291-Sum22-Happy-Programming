package swp391_sum22.happyprogramming.services;

import swp391_sum22.happyprogramming.dto.UserDTO;
import swp391_sum22.happyprogramming.exception.auth.UserAlreadyExistException;
import swp391_sum22.happyprogramming.model.User;

public interface IUserService {
    User registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException;
}
