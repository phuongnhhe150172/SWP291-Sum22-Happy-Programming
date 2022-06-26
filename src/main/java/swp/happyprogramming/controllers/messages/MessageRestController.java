package swp.happyprogramming.controllers.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.model.Message;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IMessageService;
import swp.happyprogramming.services.IUserService;

import javax.servlet.http.HttpSession;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

@Controller
public class MessageRestController {
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    @Autowired
    private IUserService userService;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private HttpSession session;

    @CrossOrigin(origins = "*")
    @GetMapping("/addImage")
    @ResponseBody
    public ResponseEntity addImage(@RequestParam("image") MultipartFile image,
                                   @RequestParam(value = "senderId", required = false) String senderId,
                                   @RequestParam(value = "receiverId", required = false) String receiverId) {
        System.out.println("Image received");
        User user = userService.getUserById(Long.parseLong(senderId));
        User receiver = userService.getUserById(Long.parseLong(receiverId));
        Message message = new Message();
        String imageUrl = messageService.uploadImage(
                Long.parseLong(senderId),
                Long.parseLong(receiverId),
                CURRENT_FOLDER,
                image
        );
        message.setContent("<img src=\"" + imageUrl + "\" alt=\"image\" class=\"rounded max-w-[20vmin]\" >");
        message.setSender(user);
        message.setReceiver(receiver);
        message.setTimestamp(Instant.now());
        messageService.saveMessage(message);
        return ResponseEntity.ok("File uploaded successfully.");
    }
}
