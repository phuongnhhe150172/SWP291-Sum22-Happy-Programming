package swp.happyprogramming.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.services.IUserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IUserService userService;

    @GetMapping("/dashboard")
    public String displayDashboardAdmin(Model model){
        int totalNumberOfMentors = userService.countUsersByRolesLike("ROLE_MENTOR");
        int totalNumberOfMentees = userService.countUsersByRolesLike("ROLE_MENTEE");

        model.addAttribute("totalNumberOfMentors", totalNumberOfMentors);
        model.addAttribute("totalNumberOfMentees", totalNumberOfMentees);
        model.addAttribute("totalNumberOfRequests", 123);

        return "admin/dashboard";
    }

    @GetMapping("/all-user")
    public String showAllUsers(Model model){
        UserDTO user = userService.showAllUsers();

        return "";
    }
}
