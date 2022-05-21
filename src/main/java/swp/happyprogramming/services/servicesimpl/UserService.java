package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import swp.happyprogramming.repository.IUserRepository;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.Role;
import swp.happyprogramming.model.User;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.services.IUserService;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private IUserRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(IUserRepository repository) {
        super();
        this.repository = repository;
    }

    public void registerNewUserAccount(UserDTO userDTO) throws UserAlreadyExistException {
        if (emailExists(userDTO.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + userDTO.getEmail());
        }
        ModelMapper mapper = new ModelMapper();
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = mapper.map(userDTO, User.class);
        user.setCreated(Date.from(Instant.now()));
        user.setModified(Date.from(Instant.now()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        repository.save(user);
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRoleToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
