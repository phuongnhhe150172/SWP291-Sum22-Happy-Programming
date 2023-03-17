package swp.happyprogramming.application.port.usecase;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.application.dto.UserAvatarDTO;
import swp.happyprogramming.application.dto.UserDTO;
import swp.happyprogramming.application.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.domain.model.Care;
import swp.happyprogramming.domain.model.Method;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.User;

public interface IUserService extends UserDetailsService {

  void registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException;

  UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException;

  int countUsersByRolesLike(String role);

  Pagination<UserAvatarDTO> getConnectionsById(long id, int pageNumber);

  List<UserAvatarDTO> getConnectionsById(long id);

  User findByEmail(String email);

  UserDTO findUserDTO(long id);

  User getUserById(Long id);

  UserDTO updateUserProfile(UserDTO userDTO);

  void removeMentee(long menteeId);

  void updateImage(Long id, MultipartFile image);

  Pagination<UserDTO> getMentees(int pageNumber, String firstName,
    String lastName, String phone, String email);

  void updateResetPasswordToken(String token, String email)
    throws UsernameNotFoundException;

  User getByResetPasswordToken(String token);

  void updatePassword(User user, String newPassword);

  void enableUser(long id);

  void disableUser(long id);

  List<Integer> getMonthlyNewMentees();

  void care(Care care);

  void deleteCare(long userId, long postId);

  int checkCared(long userId, long postId);

  List<Method> getAllMethod();

  void convertToMentor(long id);

  User save(User user);
}
