package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.ExperienceDTO;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.dto.SkillDTO;
import swp.happyprogramming.model.*;
import swp.happyprogramming.repository.*;
import swp.happyprogramming.services.IMentorService;

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

    public MentorDTO findMentor(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<Mentor> optionalUserProfile = profileRepository.findByUserID(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            Mentor profile = optionalUserProfile.get();
            User user = optionalUser.get();
            Address address = addressRepository.findByAddressId(profile.getAddressId());
            ArrayList<Experience> listExperience = experienceRepository.findByProfileId(profile.getId());
            ArrayList<Skill> listSkill = skillRepository.findAllByUserId(user.getId());
            //set data to mentorDTO
            return combineUserAndProfile(user, profile, listSkill, listExperience, address);
        } else {
            return null;
        }
    }

    private MentorDTO combineUserAndProfile(User user, Mentor profile, ArrayList<Skill> listSkill,
                                            ArrayList<Experience> listExperience, Address address) {
        ModelMapper mapper = new ModelMapper();
        MentorDTO mentorDTO = mapper.map(profile, MentorDTO.class);
        List<ExperienceDTO> listExperienceDTO = listExperience.stream().map(value -> mapper.map(value, ExperienceDTO.class)).collect(Collectors.toList());
        List<SkillDTO> listSkillDTO = listSkill.stream().map(value -> mapper.map(value, SkillDTO.class)).collect(Collectors.toList());
        Ward ward = wardRepository.findById(address.getWardID()).orElse(new Ward());
        District district = districtRepository.findById(ward.getDistrictId()).orElse(null);
        Province province = provinceRepository.findById(district.getProvinceId()).orElse(null);
        mapper.map(user, mentorDTO);
        mentorDTO.setProfileId(profile.getId());
        mentorDTO.setFullName(user.getFirstName() + " " + user.getLastName());
        mentorDTO.setExperiences((ArrayList<ExperienceDTO>) listExperienceDTO);
        mentorDTO.setSkills((ArrayList<SkillDTO>) listSkillDTO);
        mentorDTO.setWard(ward.getName());
        mentorDTO.setDistrict(district.getName());
        mentorDTO.setProvince(province.getName());

        if (address == null) {
            mentorDTO.setStreet("");
        } else {
            mentorDTO.setStreet(address.getName());
        }
        return mentorDTO;
    }

    public void updateMentor(long id, MentorDTO mentorDTO, long wardId, long wa, List<String> experienceValue, List<String> skillValue) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<Mentor> optionalUserProfile = profileRepository.findByUserID(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            Mentor profile = optionalUserProfile.get();
            User user = optionalUser.get();

            //update to table user
            updateUser(user, mentorDTO);

            //update to table user_profile
            updateUserProfile(profile, mentorDTO);

            //update to table address
            updateAddress(mentorDTO, wardId, wa, profile);

            //delete experience with mentor
            deleteExperienceAndMentorExperience(profile);

            //save experience with mentor
            saveExperienceAndMentorExperience(profile, experienceValue);

            //delete skill with user
            deleteUserSkills(user);

            //save skill with user
            if (skillValue != null) {
                saveUserSkills(user, skillValue);
            }

        }
    }

    public Map<Skill, Integer> findMapSkill(List<Skill> listSkill, List<SkillDTO> listSkillDTO) {
        Map<Skill, Integer> mapSkill = new HashMap<>();
        for (Skill skill : listSkill) {
            boolean flag = false;
            for (SkillDTO sk : listSkillDTO) {
                if (sk.getId() == skill.getId()) {
                    mapSkill.put(skill, 1);
                    flag = true;
                }
            }
            if (!flag) {
                mapSkill.put(skill, 0);
            }
        }
        return mapSkill;
    }

    private void updateUser(User user, MentorDTO mentorDTO) {
        user.setFirstName(mentorDTO.getFirstName());
        user.setLastName(mentorDTO.getLastName());
        user.setEmail(mentorDTO.getEmail());
        userRepository.save(user);
    }

    private void updateUserProfile(Mentor profile, MentorDTO mentorDTO) {
        profile.setGender(mentorDTO.getGender());
        profile.setDob(mentorDTO.getDob());
        profile.setPhoneNumber(mentorDTO.getPhoneNumber());
        profile.setBio(mentorDTO.getBio());
        profile.setSchool(mentorDTO.getSchool());
        profile.setPrice(mentorDTO.getPrice());
        profileRepository.save(profile);
    }

    private void updateAddress(MentorDTO mentorDTO, long wardId, long wa, Mentor profile) {
        Address address = addressRepository.findByAddressId(profile.getAddressId());
        address.setName(mentorDTO.getStreet());
        address.setWardID(wardId);
        addressRepository.save(address);
    }

    private void deleteExperienceAndMentorExperience(Mentor profile) {
        ArrayList<Experience> listExperience = experienceRepository.findByProfileId(profile.getId());
        List<Long> listIdExperience = listExperience.stream().map(value -> value.getId()).collect(Collectors.toList());

        listExperience.forEach(value -> profileRepository.deleteByMentorIdAndExperienceId(profile.getId(), value.getId()));
        experienceRepository.deleteAllById(listIdExperience);
    }

    private void saveExperienceAndMentorExperience(Mentor profile, List<String> experieceValue) {
        List<Experience> listExperienceWillSave = experieceValue.stream()
                .map(Experience::new).collect(Collectors.toList());

        experienceRepository.saveAll(listExperienceWillSave);
        ArrayList<Experience> listExperienceSaved = experienceRepository.findExperienceLast(listExperienceWillSave.size());

        listExperienceSaved.forEach(value ->
                profileRepository.insertByMentorIdAndExperienceId(profile.getId(), value.getId()));
    }

    private void deleteUserSkills(User user) {
        ArrayList<Skill> listSkill = skillRepository.findAllByUserId(user.getId());

        listSkill.forEach(value -> userRepository.deleteByUserIdAndSkillId(user.getId(), value.getId()));
    }

    private void saveUserSkills(User user, List<String> skillValue) {
        skillValue
                .forEach(value -> userRepository.addSkillUser(user.getId(), Long.parseLong(value)));
    }

    @Override
    public List<MentorDTO> getMentors() {
        return userRepository
                .findUsersByRole("ROLE_MENTOR")
                .stream()
                .map(user -> findMentor(user.getId()))
                .collect(Collectors.toList());
    }
}
