package swp.happyprogramming.services;

import swp.happyprogramming.model.Feedback;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.User;

import java.util.List;

public interface IFeedbackService {
    Pagination<Feedback> getFeedbackReceived(User user, int pageNumber);

    List<Feedback> getFeedbackReceived(User user);

    List<Feedback> getAllFeedBack();

    double[] feedBackCount(long userId);

    Feedback save(Feedback feedback);
}
