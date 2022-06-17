package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.dto.ConnectionDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.*;
import swp.happyprogramming.repository.*;
import swp.happyprogramming.services.IUserService;
import swp.happyprogramming.utility.Utility;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    private IMentorRepository mentorRepository;

    @Autowired
    private IWardRepository wardRepository;

    @Autowired
    private IDistrictRepository districtRepository;

    @Autowired
    private IProvinceRepository provinceRepository;

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
        //        Nguyễn Huy Hoàng - 02 - Signup
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = mapper.map(userDTO, User.class);
        Address address = new Address();
        Address savedAddress = addressRepository.save(address);
        user.setAddress(savedAddress);
        user.addRole(new Role(2));
        User savedUser = userRepository.save(user);
        Mentor mentor = new Mentor();
        mentor.setUser(savedUser);
        mentorRepository.save(mentor);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRoleToAuthorities(user.getRoles())
        );
    }

    @Override
    public int countUsersByRolesLike(String role) {
        return userRepository.countUsersByRolesLike("ROLE_MENTOR");
    }

    @Override
    public int statusRequest(long mentorId, long menteeId) {
        return userRepository.statusRequestByMentorIdAndMenteeId(mentorId, menteeId).orElse(-1);
    }

    private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    public List<ConnectionDTO> getConnectionsByEmail(String email) {
        ArrayList<User> users = userRepository.findConnectionsByEmail(email);
        return users.stream()
                .map(user -> new ConnectionDTO(
                                user.getId(),
                                user.getFirstName() + " " + user.getLastName()
                        )
                )
                .collect(Collectors.toList());
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDTO findUser(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        return Utility.mapUser(user);
    }

    @Override
    public UserDTO updateUserProfile(UserDTO userDTO, long wardId) {
        User currentUser = userRepository.getById(userDTO.getId());
        User user = mapper.map(userDTO, User.class);
        user.setEmail(currentUser.getEmail());
        user.setId(currentUser.getId());
        user.setPassword(currentUser.getPassword());
        user.getAddress().setId(currentUser.getAddress().getId());
        user.setRoles(currentUser.getRoles());
        user.setCreated(currentUser.getCreated());
        userRepository.save(user);
        updateAddress(userDTO, wardId);
        User userSaved = userRepository.findById(user.getId()).orElse(null);
        return Utility.mapUser(userSaved);
    }

    @Override
    public List<ConnectionDTO> getRequestsByEmail(String email) {
        ArrayList<User> users = userRepository.findRequestsByEmail(email);
        return users.stream()
                .map(user -> new ConnectionDTO(
                                user.getId(),
                                user.getFirstName() + " " + user.getLastName()
                        )
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> findAllMentees() {
        return userRepository
                .findUsersByRole("ROLE_MENTOR")
                .stream()
                .map(user -> findUser(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void removeMentee(long menteeId) {
        User user = userRepository.getById(menteeId);
        addressRepository.deleteById(user.getAddress().getId());
        userRepository.deleteById(menteeId);
    }

    private void updateAddress(UserDTO userDTO, long wardId) {
        Address address = Utility.mapAddressDTO(userDTO.getAddress(),wardId);
        address.setName(userDTO.getAddress().getName());
        addressRepository.save(address);
    }

    @Override
    public void updateImage(Long id, Path CURRENT_FOLDER, MultipartFile image) {
        User user = userRepository.findById(id).orElse(null);
        Path imagesPath = Paths.get("src/main/upload/static/imgs");
        String imageName = "image" + user.getId().toString() + ".jpg";
        Path imagePath = Paths.get(imageName);
        Path file = CURRENT_FOLDER.resolve(imagesPath).resolve(imagePath);
        try {
            Files.write(file,image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String imageUrl = "/upload/static/imgs/" + imageName;
        user.setImage(imageUrl);
        userRepository.save(user);
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
        User user = userRepository.findByResetPasswordToken(token);
        return user;
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder password = new BCryptPasswordEncoder();
        String Password = password.encode(newPassword);
        user.setPassword(Password);
        userRepository.save(user);
    }
}
