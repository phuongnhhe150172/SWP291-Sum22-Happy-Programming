package swp.happyprogramming.services;

import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.User;
import swp.happyprogramming.model.UserProfile;

import java.util.Optional;

public interface IUserService {
    void registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException;

    void signIn(UserDTO userDto);

    Optional<User> findMentor(long id);

    Optional<UserProfile> findProfileByUserID(long userID);
}
