package swp.happyprogramming.application.port.usecase;

import java.util.List;
import java.util.Map;
import swp.happyprogramming.adapter.dto.MentorDTO;
import swp.happyprogramming.adapter.dto.UserAvatarDTO;
import swp.happyprogramming.adapter.dto.UserDTO;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Skill;

public interface MentorUseCase {

  MentorDTO findMentor(long id);

  Pagination<MentorDTO> getMentors(int pageNumber);

  List<UserAvatarDTO> getTopMentors();

  UserDTO updateMentor(MentorDTO mentorDTO, long wardId,
    List<String> experienceValue,
    List<String> skillValue);

  void createCv(long userId, List<String> experiences,
    List<String> skills);

  Map<Skill, Integer> findMapSkill(List<Skill> skills,
    List<Skill> mentorSkill);

  List<MentorDTO> filterMentors(String word);
}
