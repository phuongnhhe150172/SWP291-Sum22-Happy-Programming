package swp.happyprogramming.adapter.port.out;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.application.port.out.ExperiencePortOut;
import swp.happyprogramming.domain.model.Experience;

@Repository("experienceRepository")
public interface IExperienceRepository extends
  JpaRepository<Experience, Long>, ExperiencePortOut {

  @Query(
    value = "SELECT e.id,e.description FROM experience as e WHERE id in (SELECT experience_id FROM mentor_experience WHERE mentor_id = ?1)",
    nativeQuery = true)
  ArrayList<Experience> findByMentorId(long id);

  @Query(value = "Select * from experience as e order by e.id desc limit ?1",
    nativeQuery = true)
  ArrayList<Experience> findExperienceLast(int number);
}
