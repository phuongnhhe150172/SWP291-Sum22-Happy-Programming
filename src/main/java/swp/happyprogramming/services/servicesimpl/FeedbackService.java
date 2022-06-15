package swp.happyprogramming.services.servicesimpl;

import org.springframework.stereotype.Service;
import swp.happyprogramming.model.Feedback;
import swp.happyprogramming.services.IFeedbackService;

import java.util.List;

@Service
public class FeedbackService implements IFeedbackService {
    @Override
    public List<Feedback> getFeedbacks(Long id) {
        return null;
    }
}
