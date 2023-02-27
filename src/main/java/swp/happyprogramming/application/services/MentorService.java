package swp.happyprogramming.application.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import swp.happyprogramming.application.dto.MentorDTO;
import swp.happyprogramming.application.dto.UserAvatarDTO;
import swp.happyprogramming.application.dto.UserDTO;
import swp.happyprogramming.application.port.out.MentorPortOut;
import swp.happyprogramming.application.port.usecase.IAddressService;
import swp.happyprogramming.application.port.usecase.IUserService;
import swp.happyprogramming.application.port.usecase.MentorUseCase;
import swp.happyprogramming.domain.model.Experience;
import swp.happyprogramming.domain.model.Mentor;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Skill;
import swp.happyprogramming.domain.model.User;
import swp.happyprogramming.utility.Utility;

@AllArgsConstructor
public class MentorService implements MentorUseCase {

  private MentorPortOut mentorRepository;
  private ModelMapper mapper;
  private IUserService userService;
  private IAddressService addressService;

  @Override
  public MentorDTO findMentor(long id) {
    Optional<Mentor> optionalMentor = mentorRepository.findByUserId(id);
    return optionalMentor.map(Utility::mapMentor).orElse(null);
  }

  @Override
  public Pagination<MentorDTO> getMentors(int pageNumber) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10);
    Page<Mentor> page = mentorRepository.findAll(pageRequest);
    return Utility.getPagination(page, Utility::mapMentor);
  }

  @Override
  public List<UserAvatarDTO> getTopMentors() {
    List<Mentor> mentors = mentorRepository.getTopMentors();
    return Utility.mapList(mentors, Utility::mapMentorToAvatarDTO);
  }

  @Override
  public UserDTO updateMentor(MentorDTO mentorDTO, long wardId,
    List<String> experienceValue,
    List<String> skillValue) {
    User user = userService.getUserById(mentorDTO.getId());
    Mentor profile = this.getMentorByUserId(mentorDTO.getId());

    updateUser(user, mentorDTO);

    profile.getExperiences().clear();
    saveExperienceAndMentorExperience(profile, experienceValue);

    profile.getSkills().clear();
    saveUserSkills(mentorDTO.getProfileId(), skillValue);

    mentorRepository.save(profile);
    User userSaved = userService.save(user);
    return Utility.mapUser(userSaved);
  }

  @Override
  public void createCv(long userId, List<String> experiences,
    List<String> skills) {
    User user = userService.getUserById(userId);
    Mentor mentor = new Mentor();
    mentor.setUser(user);

    Mentor mentorLast = mentorRepository.save(mentor);
    userService.convertToMentor(userId);

    saveExperienceAndMentorExperience(mentorLast, experiences);
    saveUserSkills(mentorLast.getId(), skills);
  }

  private Mentor getMentorByUserId(Long id) {
    return mentorRepository.findByUserId(id).orElse(null);
  }

  private void saveUserSkills(long profileId, List<String> skills) {
    if (skills == null) {
      return;
    }
    skills.forEach(
      value -> mentorRepository.addSkillUser(profileId, Long.parseLong(value)));
  }

  private void updateUser(User user, MentorDTO mentorDTO) {
    mapper.map(mentorDTO, user);
    addressService.updateAddress(user, mentorDTO.getAddress().getWard().getId(),
      mentorDTO.getAddress().getName());
  }

  private void saveExperienceAndMentorExperience(Mentor profile,
    List<String> experienceNames) {
    if (experienceNames == null) {
      return;
    }
    List<Experience> experiences = Utility.mapList(experienceNames,
      Experience::new);
    profile.setExperiences(experiences);
  }

  @Override
  public Map<Skill, Integer> findMapSkill(List<Skill> skills,
    List<Skill> mentorSkill) {
    Map<Skill, Integer> mapSkill = new HashMap<>();

    skills.forEach(skill -> {
      int count = mentorSkill.contains(skill) ? 1 : 0;
      mapSkill.put(skill, count);
    });

    return mapSkill;
  }


  @Override
  public List<MentorDTO> filterMentors(String word) {
    List<Mentor> mentors = mentorRepository.filterMentor(word);
    return Utility.mapList(mentors, Utility::mapMentor);
  }

}
