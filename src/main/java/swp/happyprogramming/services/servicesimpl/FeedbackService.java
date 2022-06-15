package swp.happyprogramming.services.servicesimpl;

import com.sun.tools.javac.util.List;
import org.springframework.stereotype.Service;
import swp.happyprogramming.model.Feedback;
import swp.happyprogramming.services.IFeedbackService;

@Service
public class FeedbackService implements IFeedbackService {
    @Override
    public List<Feedback> getFeedbacks(Long id) {
        return null;
    }
}
