package swp.happyprogramming.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import swp.happyprogramming.adapter.port.out.ISkillRepository;
import swp.happyprogramming.application.usecase.ISkillService;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Skill;
import swp.happyprogramming.utility.Utility;

@Service
public class SkillService implements ISkillService {

  @Autowired
  private ISkillRepository skillRepository;

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
