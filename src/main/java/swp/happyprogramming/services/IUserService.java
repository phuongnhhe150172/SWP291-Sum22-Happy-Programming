package swp.happyprogramming.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.dto.UserAvatarDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.User;

import java.nio.file.Path;
import java.util.List;

public interface IUserService extends UserDetailsService{
    void registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException;

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    int countUsersByRolesLike(String role);

    Pagination<UserAvatarDTO> getConnectionsById(long id, int pageNumber);
    List<UserAvatarDTO> getConnectionsById(long id);

    User findByEmail(String email);

    UserDTO findUser(long id);

    User getUserById(Long id);

    UserDTO updateUserProfile(UserDTO userDTO, long wardId);

    void removeMentee(long menteeId);

    void updateImage(Long id, Path currentFolder, MultipartFile image);

    Pagination<UserDTO> getMentees(int pageNumber, String firstName, String lastName, String phone, String email);

    void enableUser(long id);

    void disableUser(long id);

    List<Integer> getMonthlyNewMentees();
}
