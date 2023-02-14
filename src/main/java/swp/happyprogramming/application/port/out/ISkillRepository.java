package swp.happyprogramming.application.port.out;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import swp.happyprogramming.domain.model.Skill;

public interface ISkillRepository {

  Collection<Skill> searchSkills(long id);

  ArrayList<Skill> findAllByMentorId(long userId);

  Skill getById(Long id);

  Page<Skill> findAll(PageRequest pageRequest);

  List<Skill> findAll();

  Skill save(Skill skill);
}