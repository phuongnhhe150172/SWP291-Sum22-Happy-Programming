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
import swp.happyprogramming.model.*;
import swp.happyprogramming.repository.IAddressRepository;
import swp.happyprogramming.repository.IMentorRepository;
import swp.happyprogramming.repository.IUserRepository;
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
    private IMentorRepository mentorRepository;

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
        user.setAddress(savedAddress);
        user.addRole(new Role(2));
        User savedUser = userRepository.save(user);
        Mentor mentor = new Mentor();
        mentor.setUser(savedUser);
        mentorRepository.save(mentor);
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
        User user = userRepository.findByEmail(email);
        Address address = addressRepository.findByAddressId(user.getAddress().getId());
        user.setAddress(address);
        return user;
    }

    @Override
    public UserDTO findUser(UserDTO userDTO) {
        Address address = addressRepository.findByAddressId(userDTO.getAddress().getId());
        userDTO.setStreet(address.getName());
        return userDTO;
    }

    @Override
    public UserDTO findUser(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        Address address = addressRepository.findByAddressId(user.getAddress().getId());
        userDTO.setStreet(address.getName());
        return userDTO;
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
        userRepository.save(user);
        updateAddress(userDTO, wardId);
        return mapper.map(user, UserDTO.class);
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
        List<User> mentees = userRepository.findUsersByRole("ROLE_MENTEE");
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User mentee:mentees){
            UserDTO userDTO = mapper.map(mentee, UserDTO.class);
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    private void updateAddress(UserDTO userDTO, long wardId) {
        Address address = addressRepository.findByAddressId(userDTO.getAddress().getId());
        address.setName(userDTO.getStreet());
        Ward ward = new Ward();
        ward.setId(wardId);
        address.setWard(ward);
        addressRepository.save(address);
    }
}
