package swp.happyprogramming.controllers.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class SigninController {
    @GetMapping("/login")
    public String signinPage(WebRequest request, Model model) {
        return "login";
    }
}
