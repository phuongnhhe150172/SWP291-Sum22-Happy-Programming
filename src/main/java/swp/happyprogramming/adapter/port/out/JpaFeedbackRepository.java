package swp.happyprogramming.adapter.port.out;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import swp.happyprogramming.domain.model.Feedback;

public interface JpaFeedbackRepository extends JpaRepository<Feedback, Long> {

  Page<Feedback> findByReceiverOrderByCreatedDesc(User user, Pageable pageable);

  List<Feedback> findByReceiverOrderByCreatedDesc(User user);

  int countByRate(int i);

  int countByRateAndReceiverId(int i, long userId);
}
