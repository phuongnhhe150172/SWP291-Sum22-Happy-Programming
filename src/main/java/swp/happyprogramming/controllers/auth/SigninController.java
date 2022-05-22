package swp.happyprogramming.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.services.servicesimpl.UserService;

import javax.validation.Valid;

@Controller
public class SigninController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String signinPage(WebRequest request, Model model) {
        return "login";
    }

}
