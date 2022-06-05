package swp.happyprogramming.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import swp.happyprogramming.dto.ConnectionDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.User;

import java.util.List;

public interface IUserService extends UserDetailsService {
    void registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException;

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    int countUsersByRolesLike(String role);

    int statusRequest(long mentorId,long menteeId);

    List<ConnectionDTO> getConnectionsByEmail(String email);

    User findByEmail(String email);

    UserDTO findUser(UserDTO userDTO);

    UserDTO findUser(long id);

    UserDTO updateUserProfile(UserDTO userDTO, long wardId);
}
