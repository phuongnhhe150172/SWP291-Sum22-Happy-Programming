package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.ConnectionDTO;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.model.*;
import swp.happyprogramming.repository.*;
import swp.happyprogramming.services.IMentorService;
import swp.happyprogramming.utility.Utility;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    IRoleRepository roleRepository;

    @Autowired
    private IMentorRepository mentorRepository;
    private ModelMapper mapper = new ModelMapper();
    @Autowired
    private IWardRepository wardRepository;

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
            // set data to mentorDTO
            return combineUserAndProfile(user, mentor, listSkill, listExperience, address);
        } else {
            return null;
        }
    }

    private MentorDTO combineUserAndProfile(User user, Mentor mentor, ArrayList<Skill> listSkill,
                                            ArrayList<Experience> listExperience, Address address) {
        MentorDTO mentorDTO = mapper.map(mentor, MentorDTO.class);
        mapper.map(user, mentorDTO);
        mentorDTO.setProfileId(mentor.getId());
        mentorDTO.setExperiences(listExperience);
        mentorDTO.setSkills(listSkill);
        mentorDTO.setAddress(Utility.mapAddress(address));
        return mentorDTO;
    }

    @Override
    public List<MentorDTO> searchMentors(Map<String, Object> params) {
        return null;
    }

    @Override
    public Pagination<MentorDTO> getMentors(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10);
        Role role = roleRepository.findByName("ROLE_MENTOR");
        Page<User> page = userRepository.findUsersByRoles(pageRequest, role);
        int totalPages = page.getTotalPages();
        List<User> mentees = page.getContent();
        List<MentorDTO> mentorDTOS = mentees
                .stream()
                .map(user -> findMentor(user.getId()))
                .collect(Collectors.toList());
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        return new Pagination<>(mentorDTOS, pageNumbers);
    }

    @Override
    public List<ConnectionDTO> getTopMentors() {
        List<Mentor> mentors = mentorRepository.getTopMentors();
        return mentors
                .stream()
                .map(mentor -> new ConnectionDTO(
                                mentor.getUser().getId(),
                                mentor.getUser().getFirstName() + " " + mentor.getUser().getLastName(),
                                mentor.getUser().getImage()
                        )
                )
                .collect(Collectors.toList());
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

        // update to table user
        updateUser(user, mentorDTO, profile, wardId);

        // update address
        updateAddress(mentorDTO, wardId);

        // delete experience with mentor
        deleteExperienceAndMentorExperience(profile);

        // save experience with mentor
        if (experienceValue != null) {
            saveExperienceAndMentorExperience(profile, experienceValue);
        }

        // delete skill with user
        deleteUserSkills(mentorDTO.getProfileId());

        // save skill with user
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

    @Override
    public void createCv(long userId, List<String> experienceValue, List<String> skillValue) {
        User user = userRepository.findById(userId).orElse(null);
        Mentor mentor = new Mentor();
        mentor.setUser(user);

        mentorRepository.save(mentor);

        userRepository.convertToMentor(userId);

        Mentor mentorLast = mentorRepository.findMentorLast();
        if (experienceValue != null) {
            saveExperienceAndMentorExperience(mentorLast, experienceValue);
        }

        if (skillValue != null) {
            saveUserSkills(mentorLast.getId(), skillValue);
        }
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
        Address address = mapper.map(mentorDTO.getAddress(), Address.class);
        Ward ward = wardRepository.findById(wardId).orElse(new Ward());
        address.setWard(ward);
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
