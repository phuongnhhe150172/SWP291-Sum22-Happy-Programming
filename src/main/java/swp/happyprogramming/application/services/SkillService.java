package swp.happyprogramming.application.services;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import swp.happyprogramming.application.port.out.SkillPortOut;
import swp.happyprogramming.application.port.usecase.ISkillService;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Skill;
import swp.happyprogramming.utility.Utility;

@AllArgsConstructor
public class SkillService implements ISkillService {

  //@Autowired
  private SkillPortOut skillRepository;

  @Override
  public Pagination<Skill> getAllSkill(int pageNumber) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
    Page<Skill> page = skillRepository.findAll(pageRequest);
    return Utility.getPagination(page);
  }

  @Override
  public List<Skill> getAllSkill() {
    return skillRepository.findAll();
  }

  @Override
  public Skill save(Skill skill) {
    skillRepository.save(skill);
    return skill;
  }

  @Override
  public ArrayList<Skill> getAllSkillByUserId(long userId) {
    return skillRepository.findAllByMentorId(userId);
  }

  @Override
  public List<Skill> findAllByMentorId(long mentorId) {
    return skillRepository.findAllByMentorId(mentorId);
  }

  @Override
  public Skill findSkillById(long id) {
    return skillRepository.getById(id);
  }
}
