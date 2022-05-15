package swp391_sum22.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp391_sum22.happyprogramming.dao.IUserRepository;
import swp391_sum22.happyprogramming.dto.UserDTO;
import swp391_sum22.happyprogramming.model.User;
import swp391_sum22.happyprogramming.services.IUserService;

import java.util.Date;
import java.time.Instant;
import java.util.Arrays;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository repository;

    public User registerNewUserAccount(UserDTO userDTO) {
        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setCreated(Date.from(Instant.now()));
        user.setModified(Date.from(Instant.now()));

        return repository.save(user);
    }

//    private boolean emailExists(String email) {
//        return IUserRepository.findByEmail(email) != null;
//    }
}
