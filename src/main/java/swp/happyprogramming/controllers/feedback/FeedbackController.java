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
    public String showUserFeedback(Model model,
                                   @RequestParam(value = "id", required = false) String id) {
        //    show user feedback
        long userId;
        if (id == null) {
            Object sessionInfo = session.getAttribute("userInformation");
            UserDTO sessionUser = (UserDTO) sessionInfo;
            userId = sessionUser.getId();
        } else {
            userId = Integer.parseInt(id);
        }
        UserDTO viewedUser = userService.findUser(userId);
        List<Feedback> feedback = feedbackService.getFeedbackReceived(userId);
        model.addAttribute("feedback", feedback);
        model.addAttribute("viewedUser", viewedUser);
        return "feedback/feedback";
    }
}
