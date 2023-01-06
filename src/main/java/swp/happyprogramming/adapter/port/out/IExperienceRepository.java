package swp.happyprogramming.adapter.port.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.happyprogramming.domain.model.Experience;

import java.util.ArrayList;

public interface IExperienceRepository extends JpaRepository<Experience, Long> {
    @Query(
            value = "SELECT e.id,e.description FROM experience as e WHERE id in (SELECT experience_id FROM mentor_experience WHERE mentor_id = ?1)",
            nativeQuery = true)
    ArrayList<Experience> findByMentorId(long id);

    @Query(value = "Select * from experience as e order by e.id desc limit ?1",
            nativeQuery = true)
    ArrayList<Experience> findExperienceLast(int number);
}
