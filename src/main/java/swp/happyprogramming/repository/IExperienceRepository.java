package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.happyprogramming.model.Experience;

import java.util.ArrayList;

public interface IExperienceRepository extends JpaRepository<Experience, Long> {
    @Query(
            value = "SELECT e.id,e.description FROM `happyprogramming`.experience as e WHERE id in (SELECT experience_id FROM `happyprogramming`.mentor_experience WHERE mentor_id = ?1)",
            nativeQuery = true)
    ArrayList<Experience> findByProfileId(long id);
}
