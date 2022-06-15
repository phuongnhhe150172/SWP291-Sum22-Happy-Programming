package swp.happyprogramming.services;

import swp.happyprogramming.model.Skill;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ISkillService {
    List<Skill> getAllSkill();
    Skill save(Skill skill);
    ArrayList<Skill> getAllSkillByUserId(long userId);

    Skill findSkillById(long id);
}
