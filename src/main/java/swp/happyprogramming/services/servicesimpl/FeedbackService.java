package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import swp.happyprogramming.model.Feedback;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.User;
import swp.happyprogramming.repository.IFeedbackRepository;
import swp.happyprogramming.services.IFeedbackService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FeedbackService implements IFeedbackService {
    @Autowired
    private IFeedbackRepository feedbackRepository;

    @Override
    public Pagination<Feedback> getFeedbackReceived(User user, int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10);
        Page<Feedback> feedbacks = feedbackRepository.findByReceiverOrderByCreatedDesc(user, pageRequest);
        int totalPages = feedbacks.getTotalPages();
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        return new Pagination<>(feedbacks.getContent(), pageNumbers);
    }

    @Override
    public List<Feedback> getFeedbackReceived(User user) {
        return feedbackRepository.findByReceiverOrderByCreatedDesc(user);
    }

    @Override
    public List<Feedback> getAllFeedBack() {
        return feedbackRepository.findAll();
    }

    public double[] feedBackCount(long userId) {
        double[] count = new double[6];
        count[0] = feedbackRepository.countByRateAndReceiverId(1, userId);
        count[1] = feedbackRepository.countByRateAndReceiverId(2, userId);
        count[2] = feedbackRepository.countByRateAndReceiverId(3, userId);
        count[3] = feedbackRepository.countByRateAndReceiverId(4, userId);
        count[4] = feedbackRepository.countByRateAndReceiverId(5, userId);
        count[5] = count[0] + count[1] + count[2] + count[3] + count[4] + 0.1;
        return count;
    }

    @Override
    public Feedback save(Feedback feedback) {
        feedbackRepository.save(feedback);
        return feedback;
    }
}
