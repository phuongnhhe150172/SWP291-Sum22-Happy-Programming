package swp.happyprogramming.controllers.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.dto.ConnectionDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Message;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IMessageService;
import swp.happyprogramming.services.IUserService;

import javax.servlet.http.HttpSession;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public String chat(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        if (email.equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        }

        List<ConnectionDTO> connections = userService.getConnectionsByEmail(email);
        model.addAttribute("connections", connections);
        return "chat";
    }

    @GetMapping("/window")
    public String chatScreen(Model model,
                             @RequestParam(value = "id", required = false) String id) {
        // Get the connections
        Object sessionUser = session.getAttribute("userInformation");
        UserDTO user = (UserDTO) sessionUser;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<ConnectionDTO> connections = userService.getConnectionsByEmail(email);
        // Get the desired connection
        List<Long> ids = connections.stream()
                .map(ConnectionDTO::getId)
                .collect(java.util.stream.Collectors.toList());
        long receiverId;
        if (id != null && ids.contains(Long.parseLong(id))) {
            receiverId = Long.parseLong(id);
        } else {
            receiverId = ids.get(0);
        }
        ConnectionDTO receiver = connections.stream()
                .filter(connection -> connection.getId() == receiverId)
                .findFirst()
                .orElse(connections.get(0));
        // Add the connection to the model
        // Get the messages
        List<Message> messages = messageService.getMessagesByUserId(user.getId(), receiverId);
        model.addAttribute("connection", receiver);
        model.addAttribute("messages", messages);
        return "components/message-window";
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
