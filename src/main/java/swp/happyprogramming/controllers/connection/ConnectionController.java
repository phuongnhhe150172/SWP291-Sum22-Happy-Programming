package swp.happyprogramming.controllers.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.dto.UserAvatarDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.services.IConnectService;
import swp.happyprogramming.services.IUserService;

import javax.servlet.http.HttpSession;

@Controller
public class ConnectionController {
    @Autowired
    private IUserService userService;


    @Autowired
    private IConnectService connectService;

    @Autowired
    private HttpSession session;

    @Secured({"ROLE_MENTOR", "ROLE_MENTEE"})
    @GetMapping("/connections")
    public String getUserConnections(Model model, @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        // Nguyễn Huy Hoàng - 33 - view connections
        Object sessionUser = session.getAttribute("userInformation");

        UserDTO user = (UserDTO) sessionUser;

        Pagination<UserAvatarDTO> connections = userService.getConnectionsById(user.getId(), pageNumber);
        model.addAttribute("connections", connections.getPaginatedList());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", connections.getPageNumbers().size());

        return "connections";
    }

    @GetMapping("/disconnect")
    public String disConnections(Model model, @RequestParam(required = false, defaultValue = "1") long id) {
        Object sessionUser = session.getAttribute("userInformation");

        UserDTO user = (UserDTO) sessionUser;
        connectService.disconnect(id, user.getId());
        connectService.disconnect(user.getId(), id);
        return "redirect:/connections";
    }
}
