package swp.happyprogramming.services;

import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.dto.SkillDTO;
import swp.happyprogramming.model.Skill;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.List;

public interface IMentorService {
    MentorDTO findMentor(long id);

    void updateMentor(long mentorId, MentorDTO mentor, long wardId, List<String> experieceValue, List<String> skillValue);

    Map<Skill,Integer> findMapSkill(List<Skill> listSkill, ArrayList<SkillDTO> listSkillDTO);
}
