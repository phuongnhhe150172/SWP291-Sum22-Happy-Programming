package swp.happyprogramming.controllers.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swp.happyprogramming.dto.ConnectionDTO;
import swp.happyprogramming.services.IUserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ConnectionController {
    @Autowired
    private IUserService userService;

    @GetMapping("/connections")
    public String getUserConnections(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String email = authentication.getName();
        if (email.equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        }

        List<ConnectionDTO> connections = userService.getConnectionsByEmail(email);
        model.addAttribute("connections", connections);
        if (roles.contains("ROLE_MENTOR")) {
            return "mentor/connections";
        } else {
            return "mentee/connections";
        }
    }
}
