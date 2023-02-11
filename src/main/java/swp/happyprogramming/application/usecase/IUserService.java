package swp.happyprogramming.application.usecase;

import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.adapter.dto.MentorDTO;
import swp.happyprogramming.adapter.dto.MethodDTO;
import swp.happyprogramming.adapter.dto.UserAvatarDTO;
import swp.happyprogramming.adapter.dto.UserDTO;
import swp.happyprogramming.application.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.domain.model.Care;
import swp.happyprogramming.domain.model.Method;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Skill;
import swp.happyprogramming.domain.model.User;

public interface IUserService extends UserDetailsService {

  void registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException;

  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

  int countUsersByRolesLike(String role);

  Pagination<UserAvatarDTO> getConnectionsById(long id, int pageNumber);

  List<UserAvatarDTO> getConnectionsById(long id);

  User findByEmail(String email);

  UserDTO findUser(long id);

  User getUserById(Long id);

  UserDTO updateUserProfile(UserDTO userDTO);

  void removeMentee(long menteeId);

  void updateImage(Long id, MultipartFile image);

  Pagination<UserDTO> getMentees(int pageNumber, String firstName, String lastName, String phone,
    String email);

  void enableUser(long id);

  void disableUser(long id);

  List<Integer> getMonthlyNewMentees();

  Care save(Care care);

  int deleteCare(long userId, long postId);

  int checkCared(long userId, long postId);

  MentorDTO findMentor(long id);

  Pagination<MentorDTO> getMentors(int pageNumber);

  List<UserAvatarDTO> getTopMentors();

  //    UPDATE SECTION
  UserDTO updateMentor(MentorDTO mentorDTO, long wardId, List<String> experienceValue,
    List<String> skillValue);

  Map<Skill, Integer> findMapSkill(List<Skill> skills, List<Skill> mentorSkill);

  void createCv(long userId, List<String> experienceValue, List<String> skillValue);

  List<MentorDTO> filterMentors(String word);

  List<Method> getAllMethod();

  MethodDTO findMethod(long id);
}
