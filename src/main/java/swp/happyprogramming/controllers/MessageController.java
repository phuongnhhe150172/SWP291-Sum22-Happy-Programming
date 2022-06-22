package swp.happyprogramming.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import swp.happyprogramming.model.Message;

import java.text.SimpleDateFormat;
import java.time.Instant;

@Controller
public class MessageController {
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message send(Message message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(Instant.now());
        return null;
    }
}
