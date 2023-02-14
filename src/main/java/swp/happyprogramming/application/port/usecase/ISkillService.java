package swp.happyprogramming.application.port.usecase;

import java.util.ArrayList;
import java.util.List;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Skill;

public interface ISkillService {

  Pagination<Skill> getAllSkill(int pageNumber);

  List<Skill> getAllSkill();

  Skill save(Skill skill);

  ArrayList<Skill> getAllSkillByUserId(long userId);

  List<Skill> findAllByMentorId(long mentorId);

  Skill findSkillById(long id);
}
