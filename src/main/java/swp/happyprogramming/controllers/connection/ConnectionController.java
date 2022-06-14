package swp.happyprogramming.controllers.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swp.happyprogramming.dto.ConnectionDTO;
import swp.happyprogramming.services.IUserService;

import java.util.List;

@Controller
public class ConnectionController {
    @Autowired
    private IUserService userService;

    @GetMapping("/connections")
    public String getUserConnections(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        if (email.equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        }

        List<ConnectionDTO> connections = userService.getConnectionsByEmail(email);
        model.addAttribute("connections", connections);
        return "connections";
    }
}
