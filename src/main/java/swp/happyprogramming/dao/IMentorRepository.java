package swp.happyprogramming.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Mentor;
@Repository
public interface IMentorRepository extends JpaRepository<Mentor, Long> {
}
