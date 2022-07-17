package swp.happyprogramming.controllers.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Feedback;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IFeedbackService;
import swp.happyprogramming.services.IUserService;
import swp.happyprogramming.utility.Utility;
import swp.happyprogramming.vo.FeedbackVo;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpSession;

import java.time.Instant;
import java.util.ArrayList;
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
                                   @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                   @RequestParam(value = "id", required = false) String id) {
        //    show user feedback
        long userId;
        if (id == null) {
            Object sessionInfo = session.getAttribute("userInformation");
            if (sessionInfo == null) return "redirect:/login";
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
    public String addFeedback(@RequestParam String comment, @RequestParam Integer rating) {
        Object sessionInfo = session.getAttribute("userInformation");
        if (sessionInfo == null) return "redirect:/login";
        UserDTO sessionUser = (UserDTO) sessionInfo;
        long senderId = sessionUser.getId();
        UserDTO receivedUser = (UserDTO) session.getAttribute("receivedUser");

        List<Feedback> feedback = feedbackService.getFeedbackReceived(userService.getUserById(receivedUser.getId()));
        for (Feedback f : feedback) {
            if (f.getSender().getId() == sessionUser.getId()) {
                return "redirect:feedback?id=" + receivedUser.getId();
            }
        }

        Feedback feedBack = new Feedback();
        feedBack.setRate(rating);
        feedBack.setComment(comment);
        feedBack.setReceiver(userService.getUserById(receivedUser.getId()));
        feedBack.setSender(userService.getUserById(senderId));
        feedBack.setCreated(Instant.now());

        feedbackService.save(feedBack);
        return "redirect:feedback";
    }

    @GetMapping("/all-feedback")
    public String showAllFeedback(Model model) {
        //    show user feedback
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        long userId = user.getId();

        ArrayList<FeedbackVo> feedbacks = new ArrayList<>();
        List<Feedback> feedbacksRaw = feedbackService.getFeedbackReceived(userService.getUserById(userId));
        for (Feedback f : feedbacksRaw) {
            UserDTO sender = Utility.mapUser(f.getSender());
            feedbacks.add(new FeedbackVo(sender.getFirstName() + " " + sender.getLastName(), f.getRate(), f.getComment()));
        }
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("viewedUser", user);
        return "feedback/created_feedback";
    }
}
