package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.ExperienceDTO;
import swp.happyprogramming.dto.MenteeDTO;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.dto.SkillDTO;
import swp.happyprogramming.model.*;
import swp.happyprogramming.repository.*;
import swp.happyprogramming.services.IExperienceService;
import swp.happyprogramming.services.IMenteeService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenteeService implements IMenteeService {

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

    public MenteeDTO findMentee(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<UserProfile> optionalUserProfile = profileRepository.findByUserID(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            UserProfile profile = optionalUserProfile.get();
            User user = optionalUser.get();
            Address address = addressRepository.findByAddressId(profile.getAddressId());
            //set data to menteeDTO
            return combineUserAndProfile(user, profile, address);
        } else {
            return null;
        }
    }

    @Override
    public List<MenteeDTO> getAllMentees() {
        return null;
    }

    private MenteeDTO combineUserAndProfile(User user, UserProfile profile, Address address) {
        ModelMapper mapper = new ModelMapper();
        MenteeDTO menteeDTO = mapper.map(profile, MenteeDTO.class);
        Ward ward = wardRepository.findById(address.getWardID()).orElse(new Ward());
        District district = districtRepository.findById(ward.getDistrictId()).orElse(null);
        Province province = provinceRepository.findById(district.getProvinceId()).orElse(null);
        mapper.map(user, menteeDTO);
        menteeDTO.setProfileId(profile.getId());
        menteeDTO.setFullName(user.getFirstName() + " " + user.getLastName());
        menteeDTO.setWard(ward.getName());
        menteeDTO.setDistrict(district.getName());
        menteeDTO.setProvince(province.getName());

        if (address == null) {
            menteeDTO.setStreet("");
        } else {
            menteeDTO.setStreet(address.getName());
        }
        return menteeDTO;
    }

    public void updateMentee(long id, MenteeDTO menteeDTO, long wardId, long wa) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<UserProfile> optionalUserProfile = profileRepository.findByUserID(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            UserProfile profile = optionalUserProfile.get();
            User user = optionalUser.get();

            //update to table user
            updateUser(user, menteeDTO);

            //update to table user_profile
            updateUserProfile(profile, menteeDTO);

            //update to table address
            updateAddress(menteeDTO, wardId, wa, profile);


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

    private void updateUser(User user, MenteeDTO menteeDTO) {
        user.setFirstName(menteeDTO.getFirstName());
        user.setLastName(menteeDTO.getLastName());
        user.setEmail(menteeDTO.getEmail());
        userRepository.save(user);
    }

    private void updateUserProfile(UserProfile profile, MenteeDTO menteeDTO) {
        profile.setGender(menteeDTO.getGender());
        profile.setDob(menteeDTO.getDob());
        profile.setPhoneNumber(menteeDTO.getPhoneNumber());
        profile.setBio(menteeDTO.getBio());
        profile.setSchool(menteeDTO.getSchool());
        profileRepository.save(profile);
    }

    private void updateAddress(MenteeDTO menteeDTO, long wardId, long wa, UserProfile profile) {
        Address address = addressRepository.findByAddressId(profile.getAddressId());
        address.setName(menteeDTO.getStreet());
        address.setWardID(wardId);
        addressRepository.save(address);
    }

    private void deleteExperienceAndMentorExperience(UserProfile profile) {
        ArrayList<Experience> listExperience = experienceRepository.findByProfileId(profile.getId());
        List<Long> listIdExperience = listExperience.stream().map(value -> value.getId()).collect(Collectors.toList());

        listExperience.forEach(value -> profileRepository.deleteByMentorIdAndExperienceId(profile.getId(), value.getId()));
        experienceRepository.deleteAllById(listIdExperience);
    }

    private void saveExperienceAndMentorExperience(UserProfile profile, List<String> experieceValue) {
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
    public List<MenteeDTO> getMentees() {
        return userRepository
                .findUsersByRole("ROLE_MENTEE")
                .stream()
                .map(user -> findMentee(user.getId()))
                .collect(Collectors.toList());
    }
}
