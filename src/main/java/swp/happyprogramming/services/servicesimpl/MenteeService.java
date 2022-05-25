package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.MenteeDTO;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.model.*;
import swp.happyprogramming.repository.*;
import swp.happyprogramming.services.IExperienceService;

import java.util.Optional;

@Service
public class MenteeService {

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
            Address address = addressRepository.findByProfileID(profile.getId()).orElse(null);

            ModelMapper mapper = new ModelMapper();
            MenteeDTO menteeDTO = mapper.map(profile, MenteeDTO.class);

            menteeDTO.setId(user.getId());
            menteeDTO.setProfileId(profile.getId());
            menteeDTO.setFullName(user.getFirstName() + " " + user.getLastName());
            menteeDTO.setFirstName(user.getFirstName());
            menteeDTO.setLastName(user.getLastName());
            menteeDTO.setEmail(user.getEmail());

            if (address == null) {
                menteeDTO.setAddress("");
            } else {
                menteeDTO.setAddress(address.getName());
            }
            return menteeDTO;
        } else {
            return null;
        }
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

            Address address = addressRepository.findByProfileIDAndWardID(profile.getId(), wardId);
            address.setName(ward.getName() + "," + district.getName() + "," + province.getName());
            address.setWardID(wardId);
            addressRepository.save(address);
        }
    }
}
