package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dao.IProfileRepository;
import swp.happyprogramming.dao.IUserRepository;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.Role;
import swp.happyprogramming.model.UserProfile;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IUserService;

import java.util.Optional;

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
        userRepository.addUserRole(savedUser.getId(), userDTO.getRole());
        UserProfile profile = new UserProfile();
        profile.setUserID(savedUser.getId());
        profileRepository.save(profile);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
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

    public void signIn(UserDTO userDto) {

    }
}
