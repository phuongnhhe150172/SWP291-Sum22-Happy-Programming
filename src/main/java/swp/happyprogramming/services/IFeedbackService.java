package swp.happyprogramming.services;

import swp.happyprogramming.model.Feedback;
import swp.happyprogramming.model.User;

import java.util.List;

public interface IFeedbackService {
    List<Feedback> getFeedbackReceived(User user);

    double[] feedBackCount();

    Feedback save(Feedback feedback);
}
