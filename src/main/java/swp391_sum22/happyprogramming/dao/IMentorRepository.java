package swp391_sum22.happyprogramming.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp391_sum22.happyprogramming.model.Mentor;
@Repository
public interface IMentorRepository extends JpaRepository<Mentor, Long> {
}
