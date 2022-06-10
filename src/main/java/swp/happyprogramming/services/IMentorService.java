package swp.happyprogramming.services;

import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Skill;

import java.util.List;
import java.util.Map;

public interface IMentorService {
    MentorDTO findMentor(long id);

    void updateMentor(MentorDTO mentor, long wardId , List<String> experieceValue, List<String> skillValue);

    Map<Skill,Integer> findMapSkill(List<Skill> listSkill, List<Skill> listSkillDTO);

    List<MentorDTO> getMentors();
}
