package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.*;
import swp.happyprogramming.repository.*;
import swp.happyprogramming.services.IMentorService;
import swp.happyprogramming.utility.Utility;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MentorService implements IMentorService {
    @Autowired
    private IProfileRepository profileRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IAddressRepository addressRepository;

    @Autowired
    private IExperienceRepository experienceRepository;

    @Autowired
    private ISkillRepository skillRepository;

    @Autowired
    private IMentorRepository mentorRepository;

    // READ SECTION
    public MentorDTO findMentor(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<Mentor> optionalUserProfile = profileRepository.findByUserId(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            Mentor mentor = optionalUserProfile.get();
            User user = optionalUser.get();
            Address address = addressRepository.findByAddressId(user.getAddress().getId());
            ArrayList<Experience> listExperience = experienceRepository.findByMentorId(mentor.getId());
            ArrayList<Skill> listSkill = skillRepository.findAllByMentorId(mentor.getId());
            //set data to mentorDTO
            return combineUserAndProfile(user, mentor, listSkill, listExperience, address);
        } else {
            return null;
        }
    }

    private MentorDTO combineUserAndProfile(User user, Mentor profile, ArrayList<Skill> listSkill,
                                            ArrayList<Experience> listExperience, Address address) {
        ModelMapper mapper = new ModelMapper();
        MentorDTO mentorDTO = mapper.map(profile, MentorDTO.class);
        mapper.map(user, mentorDTO);
        mentorDTO.setProfileId(profile.getId());
        mentorDTO.setExperiences(listExperience);
        mentorDTO.setSkills(listSkill);
        mentorDTO.setAddress(Utility.mapAddress(address));
        return mentorDTO;
    }


    @Override
    public List<MentorDTO> getMentors() {
//        return userRepository
//                .findUsersByRole("ROLE_MENTOR")
//                .stream()
//                .map(user -> findMentor(user.getId()))
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public List<MentorDTO> searchMentors(Map<String, Object> params) {
        return null;
    }

    //    UPDATE SECTION
    public void updateMentor(MentorDTO mentorDTO, long wardId, List<String> experienceValue, List<String> skillValue) {
        Optional<User> optionalUser = userRepository.findById(mentorDTO.getId());
        Optional<Mentor> optionalUserProfile = profileRepository.findByUserId(mentorDTO.getId());
        if (!optionalUser.isPresent() || !optionalUserProfile.isPresent()) {
            return;
        }
        Mentor profile = optionalUserProfile.get();
        User user = optionalUser.get();

        //update to table user
        updateUser(user, mentorDTO, profile,wardId);

        //update address
        updateAddress(mentorDTO,wardId);

        //delete experience with mentor
        deleteExperienceAndMentorExperience(profile);

        //save experience with mentor
        if (experienceValue != null) {
            saveExperienceAndMentorExperience(profile, experienceValue);
        }

        //delete skill with user
        deleteUserSkills(mentorDTO.getProfileId());

        //save skill with user
        if (skillValue != null) {
            saveUserSkills(mentorDTO.getProfileId(), skillValue);
        }

    }

    public Map<Skill, Integer> findMapSkill(List<Skill> listSkill, List<Skill> mentorSkill) {
        Map<Skill, Integer> mapSkill = new HashMap<>();

        listSkill.forEach(skill -> {
            int count = mentorSkill.contains(skill) ? 1 : 0;
            mapSkill.put(skill, count);
        });

        return mapSkill;
    }

    private void saveUserSkills(long profileId, List<String> skillValue) {
        skillValue
                .forEach(value -> mentorRepository.addSkillUser(profileId, Long.parseLong(value)));
    }

    private void updateUser(User user, MentorDTO mentorDTO, Mentor profile, long wardId) {
        user.setFirstName(mentorDTO.getFirstName());
        user.setLastName(mentorDTO.getLastName());
        user.setSchool(mentorDTO.getSchool());
        user.setGender(mentorDTO.getGender());
        user.setPrice(mentorDTO.getPrice());
        user.setDob(mentorDTO.getDob());
        user.setPhoneNumber(mentorDTO.getPhoneNumber());
        user.setModified(Date.from(Instant.now()));
        user.setBio(mentorDTO.getBio());
        userRepository.save(user);
        profile.setModified(mentorDTO.getModified());
        mentorRepository.save(profile);
    }

    private void updateAddress(MentorDTO mentorDTO, long wardId) {
        Address address = Utility.mapAddressDTO(mentorDTO.getAddress(),wardId);
        address.setName(mentorDTO.getAddress().getName());
        addressRepository.save(address);
    }

    private void saveExperienceAndMentorExperience(Mentor profile, List<String> experieceValue) {
        List<Experience> listExperienceWillSave = experieceValue.stream()
                .map(Experience::new).collect(Collectors.toList());

        experienceRepository.saveAll(listExperienceWillSave);
        ArrayList<Experience> listExperienceSaved = experienceRepository.findExperienceLast(listExperienceWillSave.size());

        listExperienceSaved.forEach(value ->
                profileRepository.insertByMentorIdAndExperienceId(profile.getId(), value.getId()));
    }

    //    DELETE SECTION
    private void deleteExperienceAndMentorExperience(Mentor profile) {
        ArrayList<Experience> listExperience = experienceRepository.findByMentorId(profile.getId());
        List<Long> listIdExperience = listExperience.stream().map(Experience::getId).collect(Collectors.toList());

        listExperience.forEach(value -> profileRepository.deleteByMentorIdAndExperienceId(profile.getId(), value.getId()));
        experienceRepository.deleteAllById(listIdExperience);
    }


    private void deleteUserSkills(long profileId) {
        ArrayList<Skill> listSkill = skillRepository.findAllByMentorId(profileId);

        listSkill.forEach(value -> mentorRepository.deleteByUserIdAndSkillId(profileId, value.getId()));
    }


}
