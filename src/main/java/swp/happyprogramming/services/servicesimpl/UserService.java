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
import swp.happyprogramming.model.Role;
import swp.happyprogramming.model.User;
import swp.happyprogramming.model.Mentor;
import swp.happyprogramming.repository.IAddressRepository;
import swp.happyprogramming.repository.IProfileRepository;
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
        ModelMapper mapper = new ModelMapper();
        User user = mapper.map(userDTO, User.class);

        User savedUser = userRepository.save(user);

        userRepository.addRoleUser(savedUser.getId(), userDTO.getRole());

        Address address = new Address();
        Address savedAddress = addressRepository.save(address);

        Mentor profile = new Mentor();
        profile.setUserID(savedUser.getId());
        user.setAddressId(savedAddress.getId());
        profileRepository.save(profile);
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
}
