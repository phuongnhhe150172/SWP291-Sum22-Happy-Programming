package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.Role;
import swp.happyprogramming.model.User;
import swp.happyprogramming.model.UserProfile;
import swp.happyprogramming.repository.IProfileRepository;
import swp.happyprogramming.repository.IUserRepository;
import swp.happyprogramming.services.IUserService;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private IUserRepository repository;

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private IProfileRepository profileRepository;

    public UserService(IUserRepository repository) {
        super();
        this.repository = repository;
    }

    public void registerNewUserAccount(UserDTO userDTO) throws UserAlreadyExistException {
        if (emailExists(userDTO.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + userDTO.getEmail());
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        ModelMapper mapper = new ModelMapper();
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = mapper.map(userDTO, User.class);
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        User savedUser = userRepository.save(user);
        userRepository.addUserRole(savedUser.getId(), userDTO.getRole());
        UserProfile profile = new UserProfile();
        profile.setUserID(savedUser.getId());
        profileRepository.save(profile);
        user.setCreated(Date.from(Instant.now()));
        user.setModified(Date.from(Instant.now()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        repository.save(user);
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email) != null;
    }

    public boolean checkMentor(long userID) {
        return userRepository.checkMentor(userID) > 0;
    }

    public Optional<User> findMentor(long userID) {
        Optional<User> optionalUser = userRepository.findById(userID);
        if (optionalUser.isPresent() && checkMentor(userID)) {
            return optionalUser;
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserProfile> findProfileByUserID(long userID) {
        return profileRepository.findByUserID(userID);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRoleToAuthorities(user.getRoles()));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    public void signIn(UserDTO userDto) {

    }

    private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
