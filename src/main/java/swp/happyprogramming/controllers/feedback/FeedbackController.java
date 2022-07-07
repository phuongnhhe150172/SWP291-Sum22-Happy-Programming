package swp.happyprogramming.controllers.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Feedback;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IFeedbackService;
import swp.happyprogramming.services.IUserService;
import swp.happyprogramming.utility.Utility;
import swp.happyprogramming.vo.FeedbackVo;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpSession;

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
        List<Feedback> feedback = feedbackService.getFeedbackReceived(userId);
        double avgRate = Utility.getAverageRate(feedback);
        int[] count = feedbackService.feedBackCount();
        model.addAttribute("feedback", feedback);
        model.addAttribute("viewedUser", viewedUser);
        model.addAttribute("avgRate", avgRate);
        model.addAttribute("count", count);
        return "feedback/feedback";
    }

    @GetMapping("/create-feedback")
    public String createFeedback(Model model) {
        Object sessionInfo = session.getAttribute("userInformation");
        if (sessionInfo == null) return "redirect:/login";
        Integer receivedId = 22;

        UserDTO receivedUser = userService.findUser(receivedId);
        System.out.print("====send====="+receivedUser.getId());
        model.addAttribute("receivedUser", receivedUser);
        session.setAttribute("receivedUser", receivedUser);
        return "feedback/createFeedBack";
    }

    @PostMapping("/add-feedback")
    public String addFeedback(@RequestParam String comment, @RequestParam Integer rating) {
        Object sessionInfo = session.getAttribute("userInformation");
        if (sessionInfo == null) return "redirect:/login";
        UserDTO sessionUser = (UserDTO) sessionInfo;
        long senderId = sessionUser.getId();
        UserDTO receivedUser = (UserDTO)session.getAttribute("receivedUser");


        Feedback feedBack = new Feedback();
        feedBack.setRate(rating);
        feedBack.setComment(comment);
        feedBack.setReceiverid(receivedUser.getId());
        feedBack.setSenderid(senderId);

        
        feedbackService.save(feedBack);
        return "redirect:/admin/skills";
    }

    @GetMapping("/all-feedback")
    public String showAllFeedback(Model model) {
        //    show user feedback
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        long userId = user.getId();

        ArrayList<FeedbackVo> feedbacks = new ArrayList<>();
        List<Feedback> feedback_raw = feedbackService.getFeedbackReceived(userId);
        for (Feedback f :feedback_raw ) {
            UserDTO sender_user = userService.findUser(f.getSenderid());
            feedbacks.add(new FeedbackVo(sender_user.getFirstName() + " " + sender_user.getLastName(), f.getRate(), f.getComment()));
        }
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("viewedUser", user);
        return "feedback/created_feedback";
    }
}
