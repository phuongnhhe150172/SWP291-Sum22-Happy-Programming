package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.MenteeDTO;
import swp.happyprogramming.model.*;
import swp.happyprogramming.repository.*;
import swp.happyprogramming.services.IMenteeService;

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
        Optional<Mentor> optionalUserProfile = profileRepository.findByUserID(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            Mentor profile = optionalUserProfile.get();
            User user = optionalUser.get();
            Address address = addressRepository.findByAddressId(user.getAddressId());

            return combineUserAndProfile(user, profile, address);
        } else {
            return null;
        }
    }

    private MenteeDTO combineUserAndProfile(User user, Mentor profile, Address address) {
        ModelMapper mapper = new ModelMapper();
        MenteeDTO menteeDTO = mapper.map(profile, MenteeDTO.class);
        Ward ward = wardRepository.findById(address.getWardID()).orElse(null);
        District district = districtRepository.findById(ward.getDistrictId()).orElse(null);
        Province province = provinceRepository.findById(district.getProvinceId()).orElse(null);

        menteeDTO.setId(user.getId());
        menteeDTO.setProfileId(profile.getId());
//        menteeDTO.setFullName(user.getFirstName() + " " + user.getLastName());
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

    public void updateMentee(Long id, MenteeDTO menteeDTO, long wardId) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<Mentor> optionalUserProfile = profileRepository.findByUserID(id);
        if (optionalUser.isPresent() && optionalUserProfile.isPresent()) {
            User user = optionalUser.get();

            user.setFirstName(menteeDTO.getFirstName());
            user.setLastName(menteeDTO.getLastName());
            user.setEmail(menteeDTO.getEmail());
            user.setGender(menteeDTO.getGender());
            user.setDob(menteeDTO.getDob());
            user.setPhoneNumber(menteeDTO.getPhoneNumber());
            user.setBio(menteeDTO.getBio());
            user.setSchool(menteeDTO.getSchool());
            userRepository.save(user);

            Ward ward = wardRepository.findById(wardId).orElse(null);
            District district = districtRepository.findById(ward.getDistrictId()).orElse(null);
            Province province = provinceRepository.findById(district.getProvinceId()).orElse(null);

            Address address = addressRepository.findByAddressId(user.getAddressId());
            address.setName(ward.getName() + "," + district.getName() + "," + province.getName());
            address.setWardID(wardId);
            addressRepository.save(address);
        }
    }
}
