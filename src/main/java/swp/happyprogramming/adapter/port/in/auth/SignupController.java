package swp.happyprogramming.adapter.port.in.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import swp.happyprogramming.domain.dto.UserDTO;
import swp.happyprogramming.domain.exception.auth.UserAlreadyExistException;

import swp.happyprogramming.application.usecase.IUserService;

import jakarta.validation.Valid;

@Controller
public class SignupController {
    @Autowired
    private IUserService userService;

    @GetMapping("/signup")
    public String signupPage(WebRequest request, Model model) {
        // Nguyễn Huy Hoàng - 02 - Signup
        UserDTO userDto = new UserDTO();
        model.addAttribute("user", userDto);
        return "public/signup";
    }

    @PostMapping("/signup")
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDTO userDto, BindingResult errors) {
        // Nguyễn Huy Hoàng - 02 - Signup
        ModelAndView mav = new ModelAndView("signup");
        try {
            userService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistException ex) {
            mav.addObject("message", "An account for that username/email already exists.");
            return mav;
        }
        return new ModelAndView("login", "user", userDto);
    }

}