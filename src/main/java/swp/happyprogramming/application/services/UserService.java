package swp.happyprogramming.application.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import jakarta.transaction.Transactional;
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
import swp.happyprogramming.adapter.port.out.IAddressRepository;
import swp.happyprogramming.adapter.port.out.IRoleRepository;
import swp.happyprogramming.adapter.port.out.IUserRepository;
import swp.happyprogramming.adapter.port.out.IWardRepository;
import swp.happyprogramming.application.usecase.IUserService;
import swp.happyprogramming.domain.dto.UserAvatarDTO;
import swp.happyprogramming.domain.dto.UserDTO;
import swp.happyprogramming.domain.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.domain.model.Address;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Role;
import swp.happyprogramming.domain.model.User;
import swp.happyprogramming.domain.model.UserSpe;
import swp.happyprogramming.domain.model.Ward;
import swp.happyprogramming.utility.Utility;

@Service
@Transactional
public class UserService implements IUserService {
    ModelMapper mapper = new ModelMapper();

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IAddressRepository addressRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private IWardRepository wardRepository;

    public void registerNewUserAccount(UserDTO userDTO) throws UserAlreadyExistException {
        //        Nguyễn Huy Hoàng - 02 - Signup
        if (emailExists(userDTO.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + userDTO.getEmail());
        }
        saveUser(userDTO);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private void saveUser(UserDTO userDTO) {
        // Nguyễn Huy Hoàng - Signup
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = mapper.map(userDTO, User.class);
        Address address = new Address();
        Address savedAddress = addressRepository.save(address);
        Ward firstWard = wardRepository.findFirst();
        savedAddress.setWard(firstWard);

        user.setImage("/upload/static/imgs/avatar_default.jpg");
        user.setAddress(savedAddress);
        user.setStatus(1);
        user.addRole(roleRepository.findByName("ROLE_MENTEE"));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Email does not exist in system. Please re-enter another email!");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRoleToAuthorities(user.getRoles())
        );
    }

    @Override
    public int countUsersByRolesLike(String role) {
        return userRepository.countUsersByRolesLike(role);
    }

    private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    public Pagination<UserAvatarDTO> getConnectionsById(long id, int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        Page<User> page = userRepository.findConnectionsById(pageRequest, id);
        int totalPages = page.getTotalPages();
        List<User> users = page.getContent();
        List<UserAvatarDTO> connections = users.stream()
                .map(user -> new UserAvatarDTO(
                        user.getId(),
                        user.getFirstName() + " " + user.getLastName(),
                        user.getImage())
                ).collect(Collectors.toList());
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        return new Pagination<>(connections, pageNumbers);
    }

    @Override
    public List<UserAvatarDTO> getConnectionsById(long id) {
        List<User> users = userRepository.findConnectionsById(id);
        return users.stream()
                .map(user -> new UserAvatarDTO(
                        user.getId(),
                        user.getFirstName() + " " + user.getLastName(),
                        user.getImage())
                ).collect(Collectors.toList());
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public UserDTO findUser(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return null;
        return Utility.mapUser(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDTO updateUserProfile(UserDTO userDTO, long wardId) {
        User currentUser = userRepository.getById(userDTO.getId());

        Address address = currentUser.getAddress();

        Ward ward = wardRepository
                .findById(wardId)
                .orElse(null);

        address.setWard(ward);
        address.setName(userDTO.getAddress().getName());

        // migrate info to the user
        currentUser.setAddress(address);
        currentUser.setFirstName(userDTO.getFirstName());
        currentUser.setLastName(userDTO.getLastName());
        currentUser.setBio(userDTO.getBio());
        currentUser.setDob(userDTO.getDob());
        currentUser.setGender(userDTO.getGender());
        currentUser.setPhoneNumber(userDTO.getPhoneNumber());
        currentUser.setSchool(userDTO.getSchool());
        currentUser.setPrice(userDTO.getPrice());

        userRepository.save(currentUser);
        User userSaved = userRepository.findById(userDTO.getId()).orElse(new User());
        return Utility.mapUser(userSaved);
    }

    @Override
    public void removeMentee(long menteeId) {
        User user = userRepository.getById(menteeId);
        addressRepository.deleteById(user.getAddress().getId());
        userRepository.deleteById(menteeId);
    }

    @Override
    public void updateImage(Long id, Path currentFolder, MultipartFile image) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return;
        Path imagesPath = Paths.get("src/main/upload/static/imgs");
        String imageName = "image" + user.getId().toString() + ".jpg";
        Path imagePath = Paths.get(imageName);
        Path file = currentFolder.resolve(imagesPath).resolve(imagePath);
        try {
            Files.write(file, image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imageUrl = "/upload/static/imgs/" + imageName;
        user.setImage(imageUrl);
        userRepository.save(user);
    }

    @Override
    public Pagination<UserDTO> getMentees(int pageNumber, String firstName, String lastName, String phone, String email) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10, Sort.Direction.DESC, "Id");
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(new Role(2));
        Specification<User> filtered = UserSpe.getUserSpe(firstName, lastName, phone, email, roles);
        Page<User> page = userRepository.findAll(filtered, pageRequest);
        int totalPages = page.getTotalPages();
        List<User> mentees = page.getContent();
        List<UserDTO> menteesDTO = mentees.stream()
                .map(mentee -> findUser(mentee.getId()))
                .collect(Collectors.toList());
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        return new Pagination<>(menteesDTO, pageNumbers);
    }

    public void updateResetPasswordToken(String token, String email) throws UsernameNotFoundException {
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
}
