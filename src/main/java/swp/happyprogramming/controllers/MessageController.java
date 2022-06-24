package swp.happyprogramming.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        // long rec_id = Integer.parseInt(id);
        Object sessionUser = session.getAttribute("userInformation");
        UserDTO user = (UserDTO) sessionUser;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<ConnectionDTO> connections = userService.getConnectionsByEmail(email);
        List<Message> messages = messageService.getMessagesByUserId(user.getId(), connections.get(0).getId());
        model.addAttribute("connection", connections.get(0));
        model.addAttribute("messages", messages);
        return "components/message-window";
    }

    @GetMapping("/sendMessage")
    public void sendMessage(@RequestParam(value = "content", required = false) String content,
                            @RequestParam(value = "receiverId", required = false) String receiverId) {
        Object sessionUser = session.getAttribute("userInformation");
        UserDTO userDTO = (UserDTO) sessionUser;
        User user = userService.getUserById(userDTO.getId());
        Message message = new Message();
        message.setContent(content);
        message.setSender(user);
        message.setReceiver(userService.getUserById(Long.parseLong(receiverId)));
        message.setTimestamp(Instant.now());
        System.out.println(message.getContent());
        messageService.saveMessage(message);
    }
}
