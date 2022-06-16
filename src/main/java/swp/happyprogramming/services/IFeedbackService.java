package swp.happyprogramming.services;

//import com.sun.tools.javac.util.List;
import swp.happyprogramming.model.Feedback;

import java.util.List;

public interface IFeedbackService {
    List<Feedback> getFeedbacks(Long id);
}
