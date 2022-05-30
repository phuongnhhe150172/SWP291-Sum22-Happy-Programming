package swp.happyprogramming.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.User;
import swp.happyprogramming.model.UserProfile;

import java.util.Optional;

public interface IUserService extends UserDetailsService {
    void registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException;

    void signIn(UserDTO userDto);

    Optional<User> findMentor(long id);

    Optional<UserProfile> findProfileByUserID(long userID);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User findById(Long id);

    void updateResetPasswordToken(String token, String email);
    User getByResetPasswordToken(String token);
    void updatePassword(User user, String newPassword);

}
