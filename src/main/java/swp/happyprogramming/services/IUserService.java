package swp.happyprogramming.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.dto.ConnectionDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.User;

import java.nio.file.Path;
import java.util.List;

public interface IUserService extends UserDetailsService {
    void registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException;

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    int countUsersByRolesLike(String role);

    int statusRequest(long mentorId, long menteeId);

    Pagination<ConnectionDTO> getConnectionsById(long id, int pageNumber);
    List<ConnectionDTO> getConnectionsById(long id);

    User findByEmail(String email);

    UserDTO findUser(long id);

    User getUserById(long id);

    UserDTO updateUserProfile(UserDTO userDTO, long wardId);

    List<ConnectionDTO> getRequestsByEmail(String email);

    void removeMentee(long menteeId);

    void updateImage(Long id, Path currentFolder, MultipartFile image);

    Pagination<UserDTO> getMentees(int pageNumber, String firstName, String lastName, String phone, String email);

    void enableUser(long id);

    void disableUser(long id);

    List<Integer> getMonthlyNewMentees();
}
