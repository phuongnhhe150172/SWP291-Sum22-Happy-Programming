package swp.happyprogramming.application.usecase;

import swp.happyprogramming.domain.model.Feedback;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.User;

import java.util.List;

public interface IFeedbackService {
    Pagination<Feedback> getFeedbackReceived(User user, int pageNumber);

    List<Feedback> getFeedbackReceived(User user);

    List<Feedback> getAllFeedBack();

    double[] feedBackCount(long userId);

    Feedback save(Feedback feedback);
}
