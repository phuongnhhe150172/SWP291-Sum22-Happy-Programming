package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dao.IProfileRepository;
import swp.happyprogramming.dao.IUserRepository;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.UserProfile;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IUserService;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IProfileRepository profileRepository;

    public void registerNewUserAccount(UserDTO userDTO) throws UserAlreadyExistException {
        if (emailExists(userDTO.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + userDTO.getEmail());
        }
        ModelMapper mapper = new ModelMapper();
        User user = mapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        UserProfile profile = new UserProfile();
        profile.setUserID(savedUser.getId());
        profileRepository.save(profile);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
