package swp.happyprogramming.controllers.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swp.happyprogramming.dto.ConnectionDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.services.IUserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ConnectionController {
    @Autowired
    private IUserService userService;
    @Autowired
    private HttpSession session;

    @GetMapping("/connections")
    public String getUserConnections(Model model) {
        // Nguyễn Huy Hoàng - 33 - view connections
        Object sessionUser = session.getAttribute("userInformation");
        if (sessionUser == null) return "redirect:/login";

        UserDTO user = (UserDTO) sessionUser;

        List<ConnectionDTO> connections = userService.getConnectionsById(user.getId());
        model.addAttribute("connections", connections);
        return "connections";
    }
}
