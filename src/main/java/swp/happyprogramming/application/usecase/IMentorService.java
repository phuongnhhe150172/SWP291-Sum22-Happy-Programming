package swp.happyprogramming.application.usecase;

import swp.happyprogramming.domain.dto.UserAvatarDTO;
import swp.happyprogramming.domain.dto.MentorDTO;
import swp.happyprogramming.domain.dto.UserDTO;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Skill;

import java.util.List;
import java.util.Map;

public interface IMentorService {
    MentorDTO findMentor(long id);

    UserDTO updateMentor(MentorDTO mentor, long wardId , List<String> experieceValue, List<String> skillValue);

    Map<Skill,Integer> findMapSkill(List<Skill> listSkill, List<Skill> listSkillDTO);

    Pagination<MentorDTO> getMentors(int pageNumber);

    List<MentorDTO> filterMentors(String word);

    List<UserAvatarDTO> getTopMentors();

    void createCv(long userId, List<String> experienceValue, List<String> skillValue);
}
