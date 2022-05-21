package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.dao.IProfileRepository;
import swp.happyprogramming.dao.ISkillRepository;
import swp.happyprogramming.dao.IUserRepository;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.model.UserProfile;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IMentorService;

import java.util.Map;
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
        Optional<UserProfile> optionalUserProfile = profileRepository.findByUserID(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            UserProfile profile = optionalUserProfile.get();
            User user = optionalUser.get();
            ModelMapper mapper = new ModelMapper();
            MentorDTO mentorDTO = mapper.map(profile, MentorDTO.class);
            mentorDTO.setFullName(user.getFirstName() + " " + user.getLastName());
            mentorDTO.setFirstName(user.getFirstName());
            mentorDTO.setLastName(user.getLastName());
            mentorDTO.setEmail(user.getEmail());
//            Collection<Skill> skills = skillRepository.searchSkills(id);
//            mentorDTO.setSkills(new ArrayList<>(skills));
            return mentorDTO;
        } else {
            return null;
        }
    }

    public void updateMentor(Long id,MentorDTO mentorDTO){
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<UserProfile> optionalUserProfile = profileRepository.findByUserID(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            UserProfile profile = optionalUserProfile.get();
            User user = optionalUser.get();
            user.setFirstName(mentorDTO.getFirstName());
            user.setLastName(mentorDTO.getLastName());
            user.setEmail(mentorDTO.getEmail());
            userRepository.save(user);

            profile.setGender(mentorDTO.getGender());
            profile.setDob(mentorDTO.getDob());
            profile.setPhoneNumber(mentorDTO.getPhoneNumber());
            profile.setBio(mentorDTO.getBio());
            profile.setSchool(mentorDTO.getSchool());
            profile.setPrice(mentorDTO.getPrice());
            profileRepository.save(profile);
        }
    }
}
