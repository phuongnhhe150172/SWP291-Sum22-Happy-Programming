package swp.happyprogramming.application.services;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.adapter.dto.UserAvatarDTO;
import swp.happyprogramming.adapter.dto.UserDTO;
import swp.happyprogramming.application.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.application.port.out.CarePortOut;
import swp.happyprogramming.application.port.out.MentorPortOut;
import swp.happyprogramming.application.port.out.MethodPortOut;
import swp.happyprogramming.application.port.out.RolePortOut;
import swp.happyprogramming.application.port.out.UserPortOut;
import swp.happyprogramming.application.port.usecase.IAddressService;
import swp.happyprogramming.application.port.usecase.IUserService;
import swp.happyprogramming.domain.model.Address;
import swp.happyprogramming.domain.model.Care;
import swp.happyprogramming.domain.model.Method;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Role;
import swp.happyprogramming.domain.model.User;
import swp.happyprogramming.domain.model.UserSpe;
import swp.happyprogramming.utility.Utility;

@Service
@Transactional
public class UserService implements IUserService {

  @Autowired
  private ModelMapper mapper;
  @Autowired
  private MethodPortOut methodRepository;
  @Autowired
  private UserPortOut userRepository;
  @Autowired
  private IAddressService addressService;
  @Autowired
  private BCryptPasswordEncoder passwordEncoder;
  @Autowired
  private CarePortOut careRepository;
  @Autowired
  private RolePortOut roleRepository;
  @Autowired
  private MentorPortOut mentorRepository;

  @Override
  public void registerNewUserAccount(UserDTO userDTO)
    throws UserAlreadyExistException {
    if (userRepository.findByEmail(userDTO.getEmail()) != null) {
      throw new UserAlreadyExistException(
        "There is an account with that email address!");
    }
    saveUser(userDTO);
  }

  @Override
  public int countUsersByRolesLike(String role) {
    return userRepository.countUsersByRolesLike(role);
  }

  @Override
  public List<UserAvatarDTO> getConnectionsById(long id) {
    List<User> users = userRepository.findConnectionsById(id);
    return Utility.mapUsersToAvatarDTO(users);
  }

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public UserDTO updateUserProfile(UserDTO userDTO) {
    User currentUser = this.getUserById(userDTO.getId());

    updateAddress(userDTO, currentUser);
    mapper.map(userDTO, currentUser);

    userRepository.save(currentUser);
    return Utility.mapUser(currentUser);
  }

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException(
        "Email does not exist. Please re-enter another email!");
    }
    return new org.springframework.security.core.userdetails.User(
      user.getEmail(), user.getPassword(),
      mapRoleToAuthorities(user.getRoles()));
  }

  @Override
  public void removeMentee(long menteeId) {
    userRepository.deleteById(menteeId);
  }

  @Override
  public Pagination<UserDTO> getMentees(int pageNumber, String firstName,
    String lastName, String phone, String email) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10,
      Sort.Direction.DESC, "Id");
    ArrayList<Role> roles = new ArrayList<>(List.of(new Role(2)));
    Specification<User> filtered = UserSpe.getUserSpe(firstName, lastName,
      phone, email, roles);
    Page<User> page = userRepository.findAll(filtered, pageRequest);
    return Utility.getPagination(page,
      (User mentee) -> findUserDTO(mentee.getId()));
  }

  @Override
  public void updateImage(Long userId, MultipartFile image) {
    User user = this.getUserById(userId);
    if (user == null) {
      return;
    }
    String imageName = "image" + user.getId().toString() + ".jpg";
    Utility.saveImage(image, imageName);
    String imageUrl = "/upload/static/imgs/" + imageName;
    user.setImage(imageUrl);
    userRepository.save(user);
  }

  @Override
  public void updateResetPasswordToken(String token, String email)
    throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);
    if (user != null) {
      user.setResetPasswordToken(token);
      userRepository.save(user);
    } else {
      throw new UsernameNotFoundException(
        "Could not find any user with the email " + email);
    }
  }

  @Override
  public User getByResetPasswordToken(String token) {
    return userRepository.findByResetPasswordToken(token);
  }

  @Override
  public void updatePassword(User user, String newPassword) {
    BCryptPasswordEncoder password = new BCryptPasswordEncoder();
    String encodedPassword = password.encode(newPassword);
    user.setPassword(encodedPassword);
    userRepository.save(user);
  }

  @Override
  public void enableUser(long id) {
    userRepository.enableUser(id);
  }

  @Override
  public List<Integer> getMonthlyNewMentees() {
    return userRepository.getListAmountNewMentees();
  }

  @Override
  public void care(Care care) {
    careRepository.save(care);
  }

  @Override
  public void disableUser(long id) {
    userRepository.disableUser(id);
  }

  @Override
  public void deleteCare(long userId, long postId) {
    careRepository.deleteByUserIdAndPostId(userId, postId);
  }

  @Override
  public int checkCared(long userId, long postId) {
    return careRepository.findUserLikePost(postId, userId).size() > 0 ? 1 : 0;
  }

  @Override
  public List<Method> getAllMethod() {
    return methodRepository.getListMethod();
  }

  @Override
  public Pagination<UserAvatarDTO> getConnectionsById(long id, int pageNumber) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
    Page<User> page = userRepository.findConnectionsById(pageRequest, id);
    return Utility.getPagination(page, Utility::mapUserToAvatarDTO);
  }

  @Override
  public UserDTO findUserDTO(long id) {
    User user = this.getUserById(id);
    return Utility.mapUser(user);
  }

  @Override
  public User getUserById(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  @Override
  public void convertToMentor(long id) {
    userRepository.convertToMentor(id, 1);
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  private void saveUser(UserDTO userDTO) {
    userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    User user = mapper.map(userDTO, User.class);
    Address address = addressService.createNewAddress();
    user.setAddress(address);
    user.addRole(roleRepository.findByName("ROLE_MENTEE"));
    userRepository.save(user);
  }

  private Collection<? extends GrantedAuthority> mapRoleToAuthorities(
    Collection<Role> roles) {
    return roles.stream()
      .map(role -> new SimpleGrantedAuthority(role.getName()))
      .collect(Collectors.toList());
  }

  private void updateAddress(UserDTO userDTO, User currentUser) {
    addressService.updateAddress(currentUser,
      userDTO.getAddress().getWard().getId(), userDTO.getAddress().getName());
  }
}