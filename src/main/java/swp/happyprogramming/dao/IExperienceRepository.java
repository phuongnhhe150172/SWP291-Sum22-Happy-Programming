package swp.happyprogramming.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import swp.happyprogramming.model.Experience;

import java.util.ArrayList;

public interface IExperienceRepository extends JpaRepository<Experience, Long> {
    ArrayList<Experience> findByUserId(Long id);
}
