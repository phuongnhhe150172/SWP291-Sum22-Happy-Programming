package swp.happyprogramming.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;

public interface IUserService extends UserDetailsService {
    void registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException;

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    int countUsersByRolesLike(String role);

    int statusRequest(long mentorId,long menteeId);
}
