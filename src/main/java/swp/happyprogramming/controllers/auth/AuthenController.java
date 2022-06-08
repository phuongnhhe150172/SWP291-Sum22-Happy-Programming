package swp.happyprogramming.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.exception.auth.UserAlreadyExistException;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.servicesimpl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class AuthenController {
    @Autowired
    private UserService userService;

    public AuthenController(UserService userService){
        super();
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupPage(WebRequest request, Model model) {
        UserDTO userDto = new UserDTO();
        model.addAttribute("user", userDto);
        return "signup";
    }

    @PostMapping("/signup")
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDTO userDto,
                                            HttpServletRequest request,
                                            Errors errors) {
        ModelAndView mav = new ModelAndView("signup");

        try {

            User registered = userService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistException ex) {
            mav.addObject("message", "An account for that username/email already exists.");
            return mav;
        }
        return new ModelAndView("profile", "user", userDto);
    }

    @GetMapping("/login")
    public String signinPage() {
        return "login";
    }
}
