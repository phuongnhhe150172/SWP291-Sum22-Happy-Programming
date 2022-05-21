package swp.happyprogramming.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Skill;

import java.util.Collection;

@Repository
public interface ISkillRepository extends JpaRepository<Skill, Long> {
    @Query(value = "SELECT * FROM SKILLS WHERE ID IN (SELECT * FROM USER_SKILLS WHERE USER_ID = 1)",
            nativeQuery = true)
    Collection<Skill> searchSkills(long id);
}
