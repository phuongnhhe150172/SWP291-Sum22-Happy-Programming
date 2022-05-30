package swp.happyprogramming.services;

import swp.happyprogramming.model.Skill;

import java.util.ArrayList;
import java.util.List;

public interface ISkillService {
    List<Skill> getAllSkill();
    Skill save(Skill skill);
    ArrayList<Skill> getAllSkillByUserId(long userId);
}
