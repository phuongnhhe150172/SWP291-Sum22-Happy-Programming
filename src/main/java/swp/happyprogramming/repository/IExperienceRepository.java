package swp.happyprogramming.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.happyprogramming.model.Experience;

import java.util.ArrayList;

public interface IExperienceRepository extends JpaRepository<Experience, Long> {
    @Query(
            value = "SELECT * FROM Experience WHERE id in (SELECT experience_id FROM mentor_experience WHERE mentor_id = ?1)",
            nativeQuery = true)
    ArrayList<Experience> findByProfileId(Long id);
}
