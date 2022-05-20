package swp.happyprogramming.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Skill;
@Repository
public interface ISkillRepository extends JpaRepository<Skill, Integer> {
}
