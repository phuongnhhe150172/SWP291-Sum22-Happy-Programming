package swp.happyprogramming.controllers.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swp.happyprogramming.services.IUserService;

import java.security.Principal;

@Controller
public class ConnectionController {
    @Autowired
    private IUserService userService;

    @GetMapping("/connections")
    public String getUserConnections(Model model, Principal principal) {
        String email = principal.getName();
        return "index";
    }
}
