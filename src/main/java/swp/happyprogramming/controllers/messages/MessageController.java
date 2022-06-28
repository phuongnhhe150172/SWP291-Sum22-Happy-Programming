package swp.happyprogramming.controllers.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import swp.happyprogramming.dto.ConnectionDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Message;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IMessageService;
import swp.happyprogramming.services.IUserService;

import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private HttpSession session;

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

        ConnectionDTO receiver = connections.stream()
                .filter(connection -> connection.getId() == receiverId)
                .findFirst()
                .orElse(connections.get(0));

        List<Message> messages = messageService.getMessagesByUserId(user.getId(), receiverId);

        model.addAttribute("receiver", receiver);
        model.addAttribute("messages", messages);
        model.addAttribute("connections", connections);
        return "chat";
    }

    @GetMapping("/sendMessage")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessage(@RequestParam(value = "content", required = false) String content,
                            @RequestParam(value = "receiverId", required = false) String receiverId) {
        Object sessionUser = session.getAttribute("userInformation");
        UserDTO user = (UserDTO) sessionUser;
        if (sessionUser == null) return;

        User sender = userService.getUserById(user.getId());
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setReceiver(userService.getUserById(Long.parseLong(receiverId)));
        message.setTimestamp(Instant.now());
        messageService.saveMessage(message);
    }
}
