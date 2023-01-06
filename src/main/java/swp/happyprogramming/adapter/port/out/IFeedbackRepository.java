package swp.happyprogramming.adapter.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import swp.happyprogramming.domain.model.Feedback;
import swp.happyprogramming.domain.model.User;

import java.util.List;

public interface IFeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> findByReceiverOrderByCreatedDesc(User user, Pageable pageable);

    List<Feedback> findByReceiverOrderByCreatedDesc(User user);

    int countByRate(int i);

    int countByRateAndReceiverId(int i, long userId);
}
