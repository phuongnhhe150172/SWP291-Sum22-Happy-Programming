package swp.happyprogramming.services;

import com.sun.tools.javac.util.List;
import swp.happyprogramming.model.Feedback;

public interface IFeedbackService {
    List<Feedback> getFeedbacks(Long id);
}
