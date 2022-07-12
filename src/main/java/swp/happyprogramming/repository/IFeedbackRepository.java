package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swp.happyprogramming.model.Feedback;
import swp.happyprogramming.model.User;

import java.util.List;

public interface IFeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByReceiver(User user);

    int countByRate(int i);
}
