package swp391_sum22.happyprogramming.controllers.mentor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.WebRequest;
import swp391_sum22.happyprogramming.model.User;
import swp391_sum22.happyprogramming.services.servicesimpl.UserService;

@Controller
public class MentorProfileController {
    @Autowired
    private UserService userService;

    @GetMapping("/mentor/profile/{id}")
    public String getProfile(WebRequest request, Model model, @PathVariable String id) {
        try {
            long userId = Integer.parseInt(id);
            User user = userService.getUserById(userId);
            model.addAttribute("user", user);
            return "profile";
        } catch (NumberFormatException e) {
            return "redirect:/index";
        }
    }
}
