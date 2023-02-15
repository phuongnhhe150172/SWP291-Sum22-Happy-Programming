package swp.happyprogramming.adapter.port.out;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.application.port.out.SkillPortOut;
import swp.happyprogramming.domain.model.Skill;

@Repository
public interface ISkillRepository extends JpaRepository<Skill, Long>,
  SkillPortOut {

  @Query(value = "SELECT * FROM SKILLS WHERE ID IN (SELECT * FROM USER_SKILLS WHERE USER_ID = 1)",
    nativeQuery = true)
  Collection<Skill> searchSkills(long id);

  @Query(value = "SELECT * FROM SKILLS WHERE ID IN (SELECT skill_id FROM USER_SKILLS WHERE mentor_id = ?1)",
    nativeQuery = true)
  ArrayList<Skill> findAllByMentorId(long userId);

  Skill getById(Long id);

  Page<Skill> findAll(Pageable pageRequest);

  List<Skill> findAll();
}
