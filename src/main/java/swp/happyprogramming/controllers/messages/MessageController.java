package swp.happyprogramming.controllers.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.dto.ConnectionDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Message;
import swp.happyprogramming.services.IMessageService;
import swp.happyprogramming.services.IUserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    private IUserService userService;
    @Autowired
    private HttpSession session;
    @Autowired
    private IMessageService messageService;

    @GetMapping("/chat")
    public String chat(Model model, @RequestParam(value = "id", required = false) String id) {
        Object sessionUser = session.getAttribute("userInformation");
        if (sessionUser == null) return "redirect:/login";

        UserDTO user = (UserDTO) sessionUser;
        List<ConnectionDTO> connections = userService.getConnectionsById(user.getId());

        // Get the desired connection
        List<Long> ids = connections.stream()
                .map(ConnectionDTO::getId)
                .collect(java.util.stream.Collectors.toList());

        if (id == null || id.equalsIgnoreCase("null") || !ids.contains(Long.parseLong(id))) {
            return "redirect:/chat?id=" + ids.get(0);
        }

        long receiverId = Long.parseLong(id);

        List<Message> messages = messageService.getMessagesByUserId(user.getId(), receiverId);

        ConnectionDTO receiver = connections.stream()
                .filter(connection -> connection.getId() == receiverId)
                .findFirst()
                .orElse(connections.get(0));

        model.addAttribute("receiver", receiver);
        model.addAttribute("messages", messages);
        model.addAttribute("connections", connections);
        return "chat";
    }
}
