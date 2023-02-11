package swp.happyprogramming.application.usecase;

import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Skill;

import java.util.ArrayList;
import java.util.List;

public interface ISkillService {

  Pagination<Skill> getAllSkill(int pageNumber);

  List<Skill> getAllSkill();

  Skill save(Skill skill);

  ArrayList<Skill> getAllSkillByUserId(long userId);

  List<Skill> findAllByMentorId(long mentorId);

  Skill findSkillById(long id);
}
