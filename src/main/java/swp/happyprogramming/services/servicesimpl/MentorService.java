package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.model.*;
import swp.happyprogramming.repository.*;
import swp.happyprogramming.services.IExperienceService;
import swp.happyprogramming.services.IMentorService;
import swp.happyprogramming.services.ISkillService;
import swp.happyprogramming.services.IUserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MentorService implements IMentorService {
    @Autowired
    private IProfileRepository profileRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IExperienceService experienceService;

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
    private IMentorExperienceRepository mentorExperienceRepository;

    public MentorDTO findMentor(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<UserProfile> optionalUserProfile = profileRepository.findByUserID(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            UserProfile profile = optionalUserProfile.get();
            User user = optionalUser.get();
            Address address = addressRepository.findByProfileID(profile.getId()).orElse(null);

            ModelMapper mapper = new ModelMapper();
            MentorDTO mentorDTO = mapper.map(profile, MentorDTO.class);

            mentorDTO.setId(user.getId());
            mentorDTO.setProfileId(profile.getId());
            mentorDTO.setFullName(user.getFirstName() + " " + user.getLastName());
            mentorDTO.setFirstName(user.getFirstName());
            mentorDTO.setLastName(user.getLastName());
            mentorDTO.setEmail(user.getEmail());

            if (address == null) {
                mentorDTO.setAddress("");
            } else {
                mentorDTO.setAddress(address.getName());
            }
            return mentorDTO;
        } else {
            return null;
        }
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

    public void updateMentor(long id, MentorDTO mentorDTO, long wardId, List<String> experieceValue) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<UserProfile> optionalUserProfile = profileRepository.findByUserID(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            UserProfile profile = optionalUserProfile.get();
            User user = optionalUser.get();

            //update to table user
            updateUser(user,mentorDTO);

            //update to table user_profile
            updateUserProfile(profile,mentorDTO);

            //update to table address
            updateAddress(wardId,profile);

            //delete
            deleteExperienceAndMentorExperience(profile);

            //save
            saveExperienceAndMentorExperience(profile,experieceValue);
        }
    }

    private void updateUser(User user,MentorDTO mentorDTO){
        user.setFirstName(mentorDTO.getFirstName());
        user.setLastName(mentorDTO.getLastName());
        user.setEmail(mentorDTO.getEmail());
        userRepository.save(user);
    }

    private void updateUserProfile(UserProfile profile,MentorDTO mentorDTO){
        profile.setGender(mentorDTO.getGender());
        profile.setDob(mentorDTO.getDob());
        profile.setPhoneNumber(mentorDTO.getPhoneNumber());
        profile.setBio(mentorDTO.getBio());
        profile.setSchool(mentorDTO.getSchool());
        profile.setPrice(mentorDTO.getPrice());
        profileRepository.save(profile);
    }

    private void updateAddress(long wardId,UserProfile profile){
        Ward ward = wardRepository.findById(wardId).orElse(null);
        District district = districtRepository.findById(ward.getDistrictId()).orElse(null);
        Province province = provinceRepository.findById(district.getProvinceId()).orElse(null);

        Address address = addressRepository.findByProfileIDAndWardID(profile.getId(), wardId);
        address.setName(ward.getName() + "," + district.getName() + "," + province.getName());
        address.setWardID(ward.getId());
        addressRepository.save(address);
    }

    private void deleteExperienceAndMentorExperience(UserProfile profile){
        ArrayList<Experience> listExperience = experienceRepository.findByProfileId(profile.getId());
        List<Long> listIdExperience = listExperience.stream().map(value -> value.getId()).collect(Collectors.toList());
        List<MentorExperience> listMentorExperience = listExperience.stream()
                .map(value -> new MentorExperience(profile.getId(), value.getId())).collect(Collectors.toList());

        mentorExperienceRepository.deleteAll(listMentorExperience);
        experienceRepository.deleteAllById(listIdExperience);
    }

    private void saveExperienceAndMentorExperience(UserProfile profile,List<String> experieceValue){
        List<Experience> listExperienceWillSave = experieceValue.stream()
                .map(value -> new Experience(value)).collect(Collectors.toList());

        experienceRepository.saveAll(listExperienceWillSave);
        ArrayList<Experience> listExperienceSaved = experienceRepository.findExperienceLast(listExperienceWillSave.size());
        List<MentorExperience> listMentorExperienceWillSave = listExperienceSaved.stream()
                .map(value -> new MentorExperience(profile.getId(), value.getId())).collect(Collectors.toList());
        mentorExperienceRepository.saveAll(listMentorExperienceWillSave);
    }
}
