package swp.happyprogramming.adapter.port.in.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.adapter.dto.UserDTO;
import swp.happyprogramming.domain.model.Feedback;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.application.usecase.IFeedbackService;
import swp.happyprogramming.application.usecase.IUserService;
import swp.happyprogramming.utility.Utility;

import jakarta.servlet.http.HttpSession;

import java.time.Instant;
import java.util.List;

@Controller
public class FeedbackController {
    @Autowired
    private HttpSession session;
    @Autowired
    private IFeedbackService feedbackService;
    @Autowired
    private IUserService userService;

    @Secured({"ROLE_MENTOR", "ROLE_MENTEE"})
    @GetMapping("/feedback")
    public String showUserFeedback(Model model,
                                   @RequestParam(required = false, defaultValue = "1") int pageNumber,
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
        Pagination<Feedback> feedbacks = feedbackService.getFeedbackReceived(userService.getUserById(userId), pageNumber);
        double avgRate = Utility.getAverageRate(feedbacks.getPaginatedList());
        double[] count = feedbackService.feedBackCount(userId);
        model.addAttribute("feedbacks", feedbacks.getPaginatedList());
        model.addAttribute("viewedUser", viewedUser);
        model.addAttribute("avgRate", avgRate);
        model.addAttribute("count", count);
        return "feedback/feedback";
    }

    @GetMapping("/create-feedback")
    public String createFeedback(Model model, @RequestParam(value = "id") String id) {
        Object sessionInfo = session.getAttribute("userInformation");
        if (sessionInfo == null) return "redirect:/login";
        int receivedId = Integer.parseInt(id);

        UserDTO sessionUser = (UserDTO) sessionInfo;

        UserDTO receivedUser = userService.findUser(receivedId);
        List<Feedback> feedback = feedbackService.getFeedbackReceived(userService.getUserById((long) receivedId));
        for (Feedback f : feedback) {
            if (f.getSender().getId() == sessionUser.getId()) {
                return "redirect:feedback?id=" + receivedId;
            }
        }
        model.addAttribute("receivedUser", receivedUser);
        session.setAttribute("receivedUser", receivedUser);
        return "feedback/createFeedBack";
    }

    @PostMapping("/addFeedback")
    public String addFeedback(@RequestParam String comment, @RequestParam Integer rating, @RequestParam Long receiverId) {
        Object sessionInfo = session.getAttribute("userInformation");
        if (sessionInfo == null) return "redirect:/login";
        UserDTO sessionUser = (UserDTO) sessionInfo;
        long senderId = sessionUser.getId();

        List<Feedback> feedback = feedbackService.getFeedbackReceived(userService.getUserById(receiverId));
        for (Feedback f : feedback) {
            if (f.getSender().getId() == sessionUser.getId()) {
                return "redirect:feedback?id=" + receiverId;
            }
        }

        Feedback feedBack = new Feedback();
        feedBack.setRate(rating);
        feedBack.setComment(comment);
        feedBack.setReceiver(userService.getUserById(receiverId));
        feedBack.setSender(userService.getUserById(senderId));
        feedBack.setCreated(Instant.now());

        feedbackService.save(feedBack);
        return "redirect:feedback?id="+receiverId;
    }

}
