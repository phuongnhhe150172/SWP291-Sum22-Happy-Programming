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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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


    public MenteeDTO findMentee(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<UserProfile> optionalUserProfile = profileRepository.findByUserID(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            UserProfile profile = optionalUserProfile.get();
            User user = optionalUser.get();
            Address address = addressRepository.findByAddressId(profile.getAddressId());

            MenteeDTO menteeDTO = combineUserAndProfile(user,profile,address);
            return menteeDTO;
        } else {
            return null;
        }
    }

    private MenteeDTO combineUserAndProfile(User user, UserProfile profile,Address address) {
        ModelMapper mapper = new ModelMapper();
        MenteeDTO menteeDTO = mapper.map(profile, MenteeDTO.class);
        Ward ward = wardRepository.findById(address.getWardID()).orElse(null);
        District district = districtRepository.findById(ward.getDistrictId()).orElse(null);
        Province province = provinceRepository.findById(district.getProvinceId()).orElse(null);

        menteeDTO.setId(user.getId());
        menteeDTO.setProfileId(profile.getId());
        menteeDTO.setFullName(user.getFirstName() + " " + user.getLastName());
        menteeDTO.setFirstName(user.getFirstName());
        menteeDTO.setLastName(user.getLastName());
        menteeDTO.setEmail(user.getEmail());
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

    @Override
    public List<MenteeDTO> getAllMentees() {
        return userRepository.findUsersByRole("ROLE_MENTEE")
                .stream()
                .map(user -> findMentee(user.getId()))
                .collect(Collectors.toList());
    }

    public void updateMentee(Long id, MenteeDTO menteeDTO, long wardId){
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<UserProfile> optionalUserProfile = profileRepository.findByUserID(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            UserProfile profile = optionalUserProfile.get();
            User user = optionalUser.get();

            user.setFirstName(menteeDTO.getFirstName());
            user.setLastName(menteeDTO.getLastName());
            user.setEmail(menteeDTO.getEmail());
            userRepository.save(user);

            profile.setGender(menteeDTO.getGender());
            profile.setDob(menteeDTO.getDob());
            profile.setPhoneNumber(menteeDTO.getPhoneNumber());
            profile.setBio(menteeDTO.getBio());
            profile.setSchool(menteeDTO.getSchool());
            profileRepository.save(profile);

            Ward ward = wardRepository.findById(wardId).orElse(null);
            District district = districtRepository.findById(ward.getDistrictId()).orElse(null);
            Province province = provinceRepository.findById(district.getProvinceId()).orElse(null);

            Address address = addressRepository.findByAddressId(profile.getAddressId());
            address.setName(ward.getName() + "," + district.getName() + "," + province.getName());
            address.setWardID(wardId);
            addressRepository.save(address);
        }
    }
}
