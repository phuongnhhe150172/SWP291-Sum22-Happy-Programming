package swp391_sum22.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp391_sum22.happyprogramming.dao.IUserRepository;
import swp391_sum22.happyprogramming.dto.UserDTO;
import swp391_sum22.happyprogramming.exception.auth.UserAlreadyExistException;
import swp391_sum22.happyprogramming.model.User;
import swp391_sum22.happyprogramming.services.IUserService;

import java.time.Instant;
import java.util.Date;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository repository;

    public User registerNewUserAccount(UserDTO userDTO) throws UserAlreadyExistException {
        if (emailExists(userDTO.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + userDTO.getEmail());
        }
        ModelMapper mapper = new ModelMapper();
        User user = mapper.map(userDTO, User.class);

        user.setCreated(Date.from(Instant.now()));
        user.setModified(Date.from(Instant.now()));

        return repository.save(user);
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email) != null;
    }
}
