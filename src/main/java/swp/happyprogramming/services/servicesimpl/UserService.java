package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dao.IMentorRepository;
import swp.happyprogramming.dao.IUserRepository;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.Mentor;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IUserService;

import java.time.Instant;
import java.util.Date;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository repository;
    @Autowired
    private IMentorRepository mentorRepository;

    public void registerNewUserAccount(UserDTO userDTO) throws UserAlreadyExistException {
        if (emailExists(userDTO.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + userDTO.getEmail());
        }
        ModelMapper mapper = new ModelMapper();
        User user = mapper.map(userDTO, User.class);
        user.setFullName(userDTO.getFullname());
        user.setCreated(Date.from(Instant.now()));
        user.setModified(Date.from(Instant.now()));
        User savedUser = repository.save(user);
        Mentor mentor = new Mentor();
        mentor.setUserID(savedUser.getId());
        mentor.setCreated(Date.from(Instant.now()));
        mentor.setModified(Date.from(Instant.now()));
        mentorRepository.save(mentor);
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email) != null;
    }
}
