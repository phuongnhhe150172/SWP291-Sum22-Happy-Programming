package swp.happyprogramming.controllers.feedback;

import com.sun.tools.javac.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swp.happyprogramming.model.Feedback;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IFeedbackService;

import javax.servlet.http.HttpSession;

@Controller
public class FeedbackController {
    @Autowired
    private HttpSession session;
    @Autowired
    private IFeedbackService feedbackService;

    @GetMapping("/feedback")
    public String feedbackPage(Model model) {
        Object sessionInfo = session.getAttribute("userInformation");
        if (sessionInfo == null) {
            return "redirect:/login";
        }
        User sessionUser = (User) sessionInfo;
        List<Feedback> feedbacks = feedbackService.getFeedbacks(sessionUser.getId());
        model.addAttribute("feedbacks", feedbacks);
        return "feedback";
    }
}
