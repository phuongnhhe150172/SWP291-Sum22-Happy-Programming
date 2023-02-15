package swp.happyprogramming.adapter.port.out;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.application.port.out.FeedbackPortOut;
import swp.happyprogramming.domain.model.Feedback;
import swp.happyprogramming.domain.model.User;

@Repository("feedbackRepository")
public interface IFeedbackRepository extends JpaRepository<Feedback, Long> ,
  FeedbackPortOut {

  Page<Feedback> findByReceiverOrderByCreatedDesc(User user, Pageable pageable);

  List<Feedback> findByReceiverOrderByCreatedDesc(User user);

  int countByRate(int i);

  int countByRateAndReceiverId(int i, long userId);
}
