package swp.happyprogramming.adapter.port.in.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.domain.dto.UserAvatarDTO;
import swp.happyprogramming.domain.dto.UserDTO;
import swp.happyprogramming.domain.model.Message;
import swp.happyprogramming.application.usecase.IMessageService;
import swp.happyprogramming.application.usecase.IUserService;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    private IUserService userService;
    @Autowired
    private HttpSession session;
    @Autowired
    private IMessageService messageService;

    @Secured({"ROLE_MENTOR", "ROLE_MENTEE"})
    @GetMapping("/chat")
    public String chat(Model model, @RequestParam(value = "id", required = false) String id) {
        Object sessionUser = session.getAttribute("userInformation");
        UserDTO user = (UserDTO) sessionUser;

        List<UserAvatarDTO> connections = userService.getConnectionsById(user.getId());

        // Get the desired connection
        List<Long> ids = getConnectionsIds(connections);

        if (ids.isEmpty()) {
            return "redirect:/mentor";
        }

        if (id == null || !ids.contains(Long.parseLong(id))) return "redirect:/chat?id=" + ids.get(0);

        long receiverId;
        try {
            receiverId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return "redirect:/chat?id=" + ids.get(0);
        }

        List<Message> messages = messageService.getMessagesByUserId(user.getId(), receiverId);

        UserAvatarDTO receiver = getReceiver(connections, receiverId);

        model.addAttribute("receiver", receiver);
        model.addAttribute("messages", messages);
        model.addAttribute("connections", connections);
        return "chat";
    }

    private List<Long> getConnectionsIds(List<UserAvatarDTO> connections) {
        return connections.stream()
                .map(UserAvatarDTO::getId)
                .collect(java.util.stream.Collectors.toList());
    }

    private UserAvatarDTO getReceiver(List<UserAvatarDTO> connections, long receiverId) {
        return connections.stream()
                .filter(connection -> connection.getId() == receiverId)
                .findFirst()
                .orElse(connections.get(0));
    }
}
