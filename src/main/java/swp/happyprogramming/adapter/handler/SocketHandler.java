package swp.happyprogramming.adapter.handler;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import swp.happyprogramming.domain.model.Message;
import swp.happyprogramming.application.usecase.IMessageService;
import swp.happyprogramming.application.usecase.IUserService;
import swp.happyprogramming.utility.Utility;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class SocketHandler extends TextWebSocketHandler {

    private static final String PATTERN_FORMAT = "dd/MM/yyyy hh:mm";
    ArrayList<WebSocketSession> sessions = new ArrayList<>();
    Map<String, String> users = new HashMap<>();
    @Autowired
    private IMessageService messageService;
    @Autowired
    private IUserService userService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        Map value = new Gson().fromJson(message.getPayload(), Map.class);
        String senderId = (String) value.get("senderId");
        String receiverId = (String) value.get("receiverId");
        String content = (String) value.get("content");

        if (content == null) {
            users.put(senderId, session.getId());
            return;
        }
        try {
            Utility.addOG(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message savedMessage = saveMessage(value);
        value.put("timestamp", DateTimeFormatter
                .ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault())
                .format(savedMessage.getTimestamp())
        );

        for (WebSocketSession s : sessions) {
            if (s.getId().equals(users.get(receiverId)) || s.getId().equals(users.get(senderId))) {
                s.sendMessage(new TextMessage(new Gson().toJson(value)));
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    public Message saveMessage(Map value) {
        Message message = new Message();
        message.setSender(userService.getUserById(Long.parseLong((String) value.get("senderId"))));
        message.setReceiver(userService.getUserById(Long.parseLong((String) value.get("receiverId"))));
        message.setContent((String) value.get("content"));
        message.setLink(value.get("link") == null ? null : (String) value.get("link"));
        message.setTitle(value.get("title") == null ? null : (String) value.get("title"));
        message.setImage(value.get("image") == null ? null : (String) value.get("image"));
        return messageService.saveMessage(message);
    }
}