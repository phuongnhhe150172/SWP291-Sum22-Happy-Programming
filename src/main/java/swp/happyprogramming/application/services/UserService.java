package swp.happyprogramming.application.services;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import swp.happyprogramming.adapter.dto.MentorDTO;
import swp.happyprogramming.adapter.dto.MethodDTO;
import swp.happyprogramming.adapter.dto.UserAvatarDTO;
import swp.happyprogramming.adapter.dto.UserDTO;
import swp.happyprogramming.adapter.port.out.ICareRepository;
import swp.happyprogramming.adapter.port.out.IMentorRepository;
import swp.happyprogramming.adapter.port.out.IMethodRepository;
import swp.happyprogramming.adapter.port.out.IRoleRepository;
import swp.happyprogramming.adapter.port.out.IUserRepository;
import swp.happyprogramming.adapter.port.out.IWardRepository;
import swp.happyprogramming.application.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.application.usecase.IAddressService;
import swp.happyprogramming.application.usecase.IExperienceService;
import swp.happyprogramming.application.usecase.ISkillService;
import swp.happyprogramming.application.usecase.IUserService;
import swp.happyprogramming.domain.model.Address;
import swp.happyprogramming.domain.model.Care;
import swp.happyprogramming.domain.model.Experience;
import swp.happyprogramming.domain.model.Mentor;
import swp.happyprogramming.domain.model.Method;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Role;
import swp.happyprogramming.domain.model.Skill;
import swp.happyprogramming.domain.model.User;
import swp.happyprogramming.domain.model.UserSpe;
import swp.happyprogramming.domain.model.Ward;
import swp.happyprogramming.utility.Utility;

@Service
@Transactional
public class UserService implements IUserService {

  ModelMapper mapper = new ModelMapper();
  @Autowired
  private IMethodRepository methodRepository;
  @Autowired
  private IUserRepository userRepository;
  @Autowired
  private IAddressService addressService;
  @Autowired
  private BCryptPasswordEncoder passwordEncoder;
  @Autowired
  private ICareRepository careRepository;
  @Autowired
  private IRoleRepository roleRepository;
  @Autowired
  private IWardRepository wardRepository;
  @Autowired
  private IMentorRepository mentorRepository;
  @Autowired
  private IExperienceService experienceService;
  @Autowired
  private ISkillService skillService;

  public void registerNewUserAccount(UserDTO userDTO) throws UserAlreadyExistException {
    if (userRepository.findByEmail(userDTO.getEmail()) != null) {
      throw new UserAlreadyExistException("There is an account with that email address!");
    }
    saveUser(userDTO);
  }

  private void saveUser(UserDTO userDTO) {
    userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    User user = mapper.map(userDTO, User.class);
    Address address = addressService.createNewAddress();
    user.setAddress(address);
    user.addRole(roleRepository.findByName("ROLE_MENTEE"));
    userRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException("Email does not exist. Please re-enter another email!");
    }
    return new org.springframework.security.core.userdetails.User(user.getEmail(),
      user.getPassword(), mapRoleToAuthorities(user.getRoles()));
  }

  @Override
  public int countUsersByRolesLike(String role) {
    return userRepository.countUsersByRolesLike(role);
  }

  private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Collection<Role> roles) {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
      .collect(Collectors.toList());
  }

  public Pagination<UserAvatarDTO> getConnectionsById(long id, int pageNumber) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
    Page<User> page = userRepository.findConnectionsById(pageRequest, id);
    return Utility.getPagination(page, Utility::mapUserToAvatarDTO);
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

  public UserDTO findUser(long id) {
    User user = userRepository.findById(id).orElse(null);
    return Utility.mapUser(user);
  }

  public User getUserById(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  @Override
  public UserDTO updateUserProfile(UserDTO userDTO) {
    User currentUser = userRepository.getById(userDTO.getId());

    Address address = currentUser.getAddress();

    Ward ward = wardRepository.findById(userDTO.getAddress().getWard().getId()).orElse(null);
    address.setWard(ward);
    address.setName(userDTO.getAddress().getName());

    // migrate info to the user
    currentUser.setAddress(address);
    mapper.map(userDTO, currentUser);

    userRepository.save(currentUser);
    return Utility.mapUser(currentUser);
  }

  @Override
  public void removeMentee(long menteeId) {
    User user = userRepository.getById(menteeId);
    addressService.deleteAddress(user.getAddress().getId());
    userRepository.deleteById(menteeId);
  }

  @Override
  public void updateImage(Long id, MultipartFile image) {
    User user = userRepository.findById(id).orElse(null);
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
  public Pagination<UserDTO> getMentees(int pageNumber, String firstName, String lastName,
    String phone, String email) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10, Sort.Direction.DESC, "Id");
    ArrayList<Role> roles = new ArrayList<>(List.of(new Role(2)));
    Specification<User> filtered = UserSpe.getUserSpe(firstName, lastName, phone, email, roles);
    Page<User> page = userRepository.findAll(filtered, pageRequest);
    return Utility.getPagination(page, (User mentee) -> findUser(mentee.getId()));
  }

  public void updateResetPasswordToken(String token, String email)
    throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);
    if (user != null) {
      user.setResetPasswordToken(token);
      userRepository.save(user);
    } else {
      throw new UsernameNotFoundException("Could not find any user with the email " + email);
    }
  }

  public User getByResetPasswordToken(String token) {
    return userRepository.findByResetPasswordToken(token);
  }

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
  public void disableUser(long id) {
    userRepository.disableUser(id);
  }

  @Override
  public List<Integer> getMonthlyNewMentees() {
    return userRepository.getListAmountNewMentees();
  }

  @Override
  public Care save(Care care) {
    careRepository.save(care);
    return care;
  }

  @Override
  public int deleteCare(long userId, long postId) {
    careRepository.deleteByUserIdAndPostId(userId, postId);
    return 0;
  }

  @Override
  public int checkCared(long userId, long postId) {
    return careRepository.findUserLikePost(postId, userId).size() > 0 ? 1 : 0;
  }

  @Override
  public MentorDTO findMentor(long id) {
    Optional<Mentor> optionalMentor = mentorRepository.findByUserId(id);
    return optionalMentor.map(Utility::mapMentor).orElse(null);
  }

  @Override
  public Pagination<MentorDTO> getMentors(int pageNumber) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10);
    Page<Mentor> page = mentorRepository.findAll(pageRequest);
    return Utility.getPagination(page, Utility::mapMentor);
  }

  @Override
  public List<UserAvatarDTO> getTopMentors() {
    List<Mentor> mentors = mentorRepository.getTopMentors();
    return mentors.stream().map(Utility::mapMentorToAvatarDTO).collect(Collectors.toList());
  }

  @Override
  public UserDTO updateMentor(MentorDTO mentorDTO, long wardId, List<String> experienceValue,
    List<String> skillValue) {
    Optional<User> optionalUser = userRepository.findById(mentorDTO.getId());
    Optional<Mentor> optionalUserProfile = mentorRepository.findByUserId(mentorDTO.getId());
    if (optionalUser.isEmpty() || optionalUserProfile.isEmpty()) {
      return null;
    }
    Mentor profile = optionalUserProfile.get();
    User user = optionalUser.get();

    // update to table user
    updateUser(user, mentorDTO, profile);

    // update address
    updateAddress(mentorDTO);

    // delete experience with mentor
    deleteExperiences(profile);

    // save experience with mentor
    if (experienceValue != null) {
      saveExperienceAndMentorExperience(profile, experienceValue);
    }

    // delete skill with user
    deleteUserSkills(mentorDTO.getProfileId());

    // save skill with user
    if (skillValue != null) {
      saveUserSkills(mentorDTO.getProfileId(), skillValue);
    }

    User userSaved = userRepository.findById(mentorDTO.getId()).orElse(new User());
    return Utility.mapUser(userSaved);
  }

  @Override
  public Map<Skill, Integer> findMapSkill(List<Skill> skills, List<Skill> mentorSkill) {
    Map<Skill, Integer> mapSkill = new HashMap<>();

    skills.forEach(skill -> {
      int count = mentorSkill.contains(skill) ? 1 : 0;
      mapSkill.put(skill, count);
    });

    return mapSkill;
  }

  @Override
  public void createCv(long userId, List<String> experiences, List<String> skills) {
    User user = userRepository.findById(userId).orElse(null);
    Mentor mentor = new Mentor();
    mentor.setUser(user);

    mentorRepository.save(mentor);

    userRepository.convertToMentor(userId, 1);

    Mentor mentorLast = mentorRepository.findMentorLast();
    if (experiences != null) {
      saveExperienceAndMentorExperience(mentorLast, experiences);
    }

    saveUserSkills(mentorLast.getId(), skills);
  }

  private void saveUserSkills(long profileId, List<String> skills) {
    if (skills == null) {
      return;
    }
    skills.forEach(value -> mentorRepository.addSkillUser(profileId, Long.parseLong(value)));
  }

  private void updateUser(User user, MentorDTO mentorDTO, Mentor profile) {
    user.setFirstName(mentorDTO.getFirstName());
    user.setLastName(mentorDTO.getLastName());
    user.setSchool(mentorDTO.getSchool());
    user.setGender(mentorDTO.getGender());
    user.setPrice(mentorDTO.getPrice());
    user.setDob(mentorDTO.getDob());
    user.setPhoneNumber(mentorDTO.getPhoneNumber());
    user.setBio(mentorDTO.getBio());
    profile.setModified(mentorDTO.getModified());
    user.markModified();
    userRepository.save(user);
    mentorRepository.save(profile);
  }

  private void updateAddress(MentorDTO mentorDTO) {
    Address address = mapper.map(mentorDTO.getAddress(), Address.class);
    Ward ward = wardRepository.findById(mentorDTO.getAddress().getWard().getId())
      .orElse(new Ward());
    address.setWard(ward);
    address.setName(mentorDTO.getAddress().getName());
    addressService.saveAddress(address);
  }

  private void saveExperienceAndMentorExperience(Mentor profile, List<String> experiences) {
    if (experiences == null) {
      return;
    }
    List<Experience> listExperienceWillSave = experiences.stream().map(Experience::new)
      .collect(Collectors.toList());

    List<Experience> listExperienceSaved = experienceService.saveAll(listExperienceWillSave);

    listExperienceSaved.forEach(
      value -> mentorRepository.insertByMentorIdAndExperienceId(profile.getId(), value.getId()));
  }

  private void deleteExperiences(Mentor profile) {
    experienceService.deleteExperienceByMentorId(profile.getId());
  }

  private void deleteUserSkills(long profileId) {
    List<Skill> skills = skillService.findAllByMentorId(profileId);
    skills.forEach(value -> mentorRepository.deleteByUserIdAndSkillId(profileId, value.getId()));
  }

  @Override
  public List<MentorDTO> filterMentors(String word) {
    List<Mentor> mentors = mentorRepository.filterMentor(word);
    return Utility.mapMentors(mentors);
  }

  @Override
  public List<Method> getAllMethod() {
    return methodRepository.getListMethod();
  }

  @Override
  public MethodDTO findMethod(long id) {
    Method method = methodRepository.findById(id);
    if (method == null) {
      return null;
    }
    return mapper.map(method, MethodDTO.class);
  }
}
