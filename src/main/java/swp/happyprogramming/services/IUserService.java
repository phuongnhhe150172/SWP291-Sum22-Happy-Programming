package swp.happyprogramming.services;

import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;

public interface IUserService {
    void registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException;

    void signIn(UserDTO userDto);
}
