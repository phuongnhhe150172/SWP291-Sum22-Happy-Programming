package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.ConnectionDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.Address;
import swp.happyprogramming.model.Mentor;
import swp.happyprogramming.model.Role;
import swp.happyprogramming.model.User;
import swp.happyprogramming.repository.*;
import swp.happyprogramming.services.IUserService;

import javax.transaction.Transactional;
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
    private IProfileRepository profileRepository;

    public void registerNewUserAccount(UserDTO userDTO) throws UserAlreadyExistException {
        if (emailExists(userDTO.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + userDTO.getEmail());
        }
        saveUser(userDTO);
    }

    private void saveUser(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = mapper.map(userDTO, User.class);
        Address address = new Address();
        Address savedAddress = addressRepository.save(address);
        user.setAddressId(savedAddress.getId());
        user.setRoles(new Role(2));
        User savedUser = userRepository.save(user);
        Mentor mentor = new Mentor();
        mentor.setUserID(savedUser.getId());
        profileRepository.save(mentor);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRoleToAuthorities(user.getRoles()));
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
    public UserDTO findUser(UserDTO userDTO){
        userDTO.setFullName(userDTO.getFirstName() + userDTO.getLastName());
        Address address = addressRepository.findByAddressId(userDTO.getAddressId());
        userDTO.setStreet(address.getName());
        return userDTO;
    }

    @Override
    public UserDTO updateUserProfile(UserDTO userDTO,UserDTO user, long wardId){
        User use = mapper.map(user, User.class);

        updateUser(use,userDTO);

        updateAddress(userDTO,wardId,use);
        return mapper.map(use,UserDTO.class);
    }

    private void updateUser(User user, UserDTO userDTO) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setBio(userDTO.getBio());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setDob(userDTO.getDob());
        user.setPrice(userDTO.getPrice());
        user.setGender(userDTO.getGender());
        user.setSchool(userDTO.getSchool());
        userRepository.save(user);
    }

    private void updateAddress(UserDTO userDTO, long wardId, User user) {
        Address address = addressRepository.findByAddressId(user.getAddressId());
        address.setName(userDTO.getStreet());
        address.setWardID(wardId);
        addressRepository.save(address);
    }
}
