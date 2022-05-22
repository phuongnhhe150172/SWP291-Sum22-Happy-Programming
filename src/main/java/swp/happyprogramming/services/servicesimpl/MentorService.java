package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.repository.IProfileRepository;
import swp.happyprogramming.repository.ISkillRepository;
import swp.happyprogramming.repository.IUserRepository;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.model.UserProfile;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IExperienceService;
import swp.happyprogramming.services.IMentorService;
import swp.happyprogramming.services.ISkillService;
import swp.happyprogramming.services.IUserService;

import java.util.Optional;

@Service
public class MentorService implements IMentorService {
    @Autowired
    private IUserService userService;
    @Autowired
    private ISkillService skillService;
    @Autowired
    private IExperienceService experienceService;


    public MentorDTO findMentor(long userID) {
        Optional<User> optionalUser = userService.findMentor(userID);
        Optional<UserProfile> optionalUserProfile = userService.findProfileByUserID(userID);
        if (!optionalUser.isPresent() || !optionalUserProfile.isPresent()) {
            return null;
        }
        UserProfile profile = optionalUserProfile.get();
        User user = optionalUser.get();
        return combineUserAndProfile(user, profile);
    }

    public MentorDTO combineUserAndProfile(User user, UserProfile profile) {
        ModelMapper mapper = new ModelMapper();
        MentorDTO mentorDTO = mapper.map(profile, MentorDTO.class);
        mentorDTO.setExperiences(experienceService.getAllExperienceByProfileID(profile.getId()));
        mentorDTO.setFirstName(user.getFirstName());
        mentorDTO.setLastName(user.getLastName());
        mentorDTO.setEmail(user.getEmail());
        return mentorDTO;
    }
}
