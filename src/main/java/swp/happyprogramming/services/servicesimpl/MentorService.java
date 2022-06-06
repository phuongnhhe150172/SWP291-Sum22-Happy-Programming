package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.model.*;
import swp.happyprogramming.repository.*;
import swp.happyprogramming.services.IMentorService;

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
    private IProvinceRepository provinceRepository;

    @Autowired
    private IDistrictRepository districtRepository;

    @Autowired
    private IWardRepository wardRepository;

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
        Ward ward = wardRepository.findById(address.getWard().getId()).orElse(new Ward());
        District district = districtRepository.findById(ward.getDistrict().getId()).orElse(new District());
        Province province = provinceRepository.findById(district.getProvince().getId()).orElse(new Province());
        mapper.map(user, mentorDTO);
        mentorDTO.setProfileId(profile.getId());
        mentorDTO.setExperiences(listExperience);
        mentorDTO.setSkills(listSkill);
        mentorDTO.setWard(ward.getName());
        mentorDTO.setDistrict(district.getName());
        mentorDTO.setProvince(province.getName());
        // The street name is set to "" by default
        mentorDTO.setStreet(address.getName());
        return mentorDTO;
    }


    @Override
    public List<MentorDTO> getMentors() {
        return userRepository
                .findUsersByRole("ROLE_MENTOR")
                .stream()
                .map(user -> findMentor(user.getId()))
                .collect(Collectors.toList());
    }

    //    UPDATE SECTION
    public void updateMentor(MentorDTO mentorDTO, long wardId, List<String> experienceValue, List<String> skillValue) {
        Optional<User> optionalUser = userRepository.findById(mentorDTO.getId());
        Optional<Mentor> optionalUserProfile = profileRepository.findByUserId(mentorDTO.getId());
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            Mentor profile = optionalUserProfile.get();
            User user = optionalUser.get();

            //update to table user
            updateUser(user, mentorDTO);

            //update to table address
            updateAddress(mentorDTO, wardId, user);

            //delete experience with mentor
            deleteExperienceAndMentorExperience(profile);

            //save experience with mentor
            if(experienceValue != null){
                saveExperienceAndMentorExperience(profile, experienceValue);
            }

            //delete skill with user
            deleteUserSkills(mentorDTO.getProfileId());

            //save skill with user
            if (skillValue != null) {
                saveUserSkills(mentorDTO.getProfileId(), skillValue);
            }

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

    private void updateUser(User user, MentorDTO mentorDTO) {
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
    }

    private void saveExperienceAndMentorExperience(Mentor profile, List<String> experieceValue) {
        List<Experience> listExperienceWillSave = experieceValue.stream()
                .map(value -> new Experience(value)).collect(Collectors.toList());

        experienceRepository.saveAll(listExperienceWillSave);
        ArrayList<Experience> listExperienceSaved = experienceRepository.findExperienceLast(listExperienceWillSave.size());

        listExperienceSaved.forEach(value ->
                profileRepository.insertByMentorIdAndExperienceId(profile.getId(), value.getId()));
    }

    private void updateAddress(MentorDTO mentorDTO, long wardId, User user) {
        Address address = addressRepository.findByAddressId(user.getAddress().getId());
        address.setName(mentorDTO.getStreet());
        address.getWard().setId(wardId);
        addressRepository.save(address);
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
