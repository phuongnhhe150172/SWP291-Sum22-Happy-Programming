package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.model.Feedback;
import swp.happyprogramming.model.User;
import swp.happyprogramming.repository.IFeedbackRepository;
import swp.happyprogramming.services.IFeedbackService;

import java.util.List;

@Service
public class FeedbackService implements IFeedbackService {
    @Autowired
    private IFeedbackRepository feedbackRepository;

    @Override
    public List<Feedback> getFeedbackReceived(User user) {
        return feedbackRepository.findByReceiver(user);
    }

    public double[] feedBackCount() {
        double[] count = new double[6];
        count[0] = feedbackRepository.countByRate(1);
        count[1] = feedbackRepository.countByRate(2);
        count[2] = feedbackRepository.countByRate(3);
        count[3] = feedbackRepository.countByRate(4);
        count[4] = feedbackRepository.countByRate(5);
        count[5] = count[0] + count[1] + count[2] + count[3] + count[4] + 0.1;
        return count;
    }

    @Override
    public Feedback save(Feedback feedback) {
        feedbackRepository.save(feedback);
        return feedback;
    }
}
