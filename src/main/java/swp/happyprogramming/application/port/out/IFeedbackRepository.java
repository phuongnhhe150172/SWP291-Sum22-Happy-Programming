package swp.happyprogramming.application.port.out;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import swp.happyprogramming.domain.model.Feedback;
import swp.happyprogramming.domain.model.User;

public interface IFeedbackRepository {

  Page<Feedback> findByReceiverOrderByCreatedDesc(User user, Pageable pageable);

  List<Feedback> findByReceiverOrderByCreatedDesc(User user);

  int countByRate(int i);

  int countByRateAndReceiverId(int i, long userId);

  List<Feedback> findAll();

  Feedback save(Feedback feedback);
}
