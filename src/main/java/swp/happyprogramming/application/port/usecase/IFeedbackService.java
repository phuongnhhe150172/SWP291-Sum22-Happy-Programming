package swp.happyprogramming.application.port.usecase;

import java.util.List;
import swp.happyprogramming.domain.model.Feedback;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.User;

public interface IFeedbackService {

  Pagination<Feedback> getFeedbackReceived(User user, int pageNumber);

  List<Feedback> getFeedbackReceived(User user);

  List<Feedback> getAllFeedBack();

  double[] feedBackCount(long userId);

  Feedback save(Feedback feedback);
}
