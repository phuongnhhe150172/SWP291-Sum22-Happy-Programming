package swp.happyprogramming.services;

import swp.happyprogramming.dto.UserAvatarDTO;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Skill;

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
