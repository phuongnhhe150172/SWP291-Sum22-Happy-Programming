package swp.happyprogramming.services;

import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Skill;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ISkillService {
    Pagination<Skill> getAllSkill(int pageNumber);
    List<Skill> getAllSkill();
    Skill save(Skill skill);
    ArrayList<Skill> getAllSkillByUserId(long userId);

    Skill findSkillById(long id);
}
