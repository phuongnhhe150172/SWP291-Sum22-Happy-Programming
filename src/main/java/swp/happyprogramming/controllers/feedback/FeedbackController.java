package swp.happyprogramming.controllers.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Feedback;
import swp.happyprogramming.services.IFeedbackService;
import swp.happyprogramming.services.IUserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class FeedbackController {
    @Autowired
    private HttpSession session;
    @Autowired
    private IFeedbackService feedbackService;
    @Autowired
    private IUserService userService;

    @GetMapping("/feedback")
    public String feedbackPage(Model model) {
        //        Nguyễn Huy Hoàng - 16 - View user feedback
        Object sessionInfo = session.getAttribute("userInformation");
        if (sessionInfo == null) {
            return "redirect:/login";
        }
        UserDTO sessionUser = (UserDTO) sessionInfo;
        List<Feedback> feedbacks = feedbackService.getFeedbackReceived(sessionUser.getId());
        int[] count = feedbackService.feedBackCount();
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("count", count);
        return "feedback/feedback";
    }

    @GetMapping("/feedback")
    public String showUserFeedback(Model model,
                                   @RequestParam(value = "id") String id) {
        //    show user feedback
        if (id == null) return "redirect:/login";

        long userId = Integer.parseInt(id);
        UserDTO viewedUser = userService.findUser(userId);
        List<Feedback> feedback = feedbackService.getFeedbackReceived(userId);
        model.addAttribute("feedback", feedback);
        model.addAttribute("viewedUser", viewedUser);
        return "feedback/feedback";
    }
}
