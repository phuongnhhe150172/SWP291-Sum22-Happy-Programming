package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dao.IProfileRepository;
import swp.happyprogramming.dao.ISkillRepository;
import swp.happyprogramming.dao.IUserRepository;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.model.UserProfile;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IMentorService;

import java.util.Optional;

@Service
public class MentorService implements IMentorService {
    @Autowired
    private IProfileRepository profileRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ISkillRepository skillRepository;

    public MentorDTO findMentor(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<UserProfile> optionalUserProfile = profileRepository.findById(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            UserProfile profile = optionalUserProfile.get();
            User user = optionalUser.get();
            ModelMapper mapper = new ModelMapper();
            MentorDTO mentorDTO = mapper.map(profile, MentorDTO.class);
            mentorDTO.setFullName(user.getFirstName() + " " + user.getLastName());
//            Collection<Skill> skills = skillRepository.searchSkills(id);
//            mentorDTO.setSkills(new ArrayList<>(skills));
            return mentorDTO;
        } else {
            return null;
        }
    }
}
