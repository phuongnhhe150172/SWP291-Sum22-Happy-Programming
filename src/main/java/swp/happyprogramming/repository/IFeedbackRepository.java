package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swp.happyprogramming.model.Feedback;

import java.util.List;

public interface IFeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByReceiverid(Long id);

    int countByRate(int i);
}
