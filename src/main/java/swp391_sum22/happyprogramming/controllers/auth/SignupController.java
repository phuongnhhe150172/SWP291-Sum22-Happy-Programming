package swp391_sum22.happyprogramming.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import swp391_sum22.happyprogramming.dto.UserDTO;
import swp391_sum22.happyprogramming.exception.auth.UserAlreadyExistException;
import swp391_sum22.happyprogramming.model.User;
import swp391_sum22.happyprogramming.services.IMentorService;
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
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDTO userDto) {
        ModelAndView mav = new ModelAndView("signup");
        try {
            userService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistException ex) {
            mav.addObject("message", "An account for that username/email already exists.");
            return mav;
        }
        return new ModelAndView("profile", "user", userDto);
    }
}
