package swp391_sum22.happyprogramming.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import swp391_sum22.happyprogramming.dto.UserDTO;
import swp391_sum22.happyprogramming.model.User;
import swp391_sum22.happyprogramming.services.servicesimpl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class SignupController {
    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signupPage(WebRequest request, Model model) {
        UserDTO userDto = new UserDTO();
        model.addAttribute("user", userDto);
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") @Valid UserDTO userDto,
                         HttpServletRequest request,
                         Errors errors) {
        try {
            User registered = userService.registerNewUserAccount(userDto);
            return "profile";
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return "signup";
    }
}
