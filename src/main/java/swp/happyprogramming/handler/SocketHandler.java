package swp.happyprogramming.handler;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import swp.happyprogramming.model.Message;
import swp.happyprogramming.services.IMessageService;
import swp.happyprogramming.services.IUserService;
import swp.happyprogramming.utility.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class SocketHandler extends TextWebSocketHandler {

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

        sessions.stream()
                .filter(s -> s.getId().equals(users.get(receiverId)) || s.getId().equals(users.get(senderId)))
                .forEach(target -> sendMessage(value, content, target));

        saveMessage(senderId, receiverId, content);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    public void saveMessage(String senderId, String receiverId, String content) throws IOException {
        Message message = new Message();
        message.setSender(userService.getUserById(Long.parseLong(senderId)));
        message.setReceiver(userService.getUserById(Long.parseLong(receiverId)));
        message.setContent(content);
        String firstURL = Utility.getFirstLink(content);
        String[] og = Utility.getOG(firstURL);
        if (!firstURL.isEmpty()) message.setLink(firstURL);
        if (og.length != 0) {
            message.setTitle(og[0]);
            message.setImage(og[1]);
        }
        messageService.saveMessage(message);
    }

    public void sendMessage(Map value, String content, WebSocketSession target) {
        try {
            String firstURL = Utility.getFirstLink(content);
            String[] og = Utility.getOG(firstURL);
            if (!firstURL.isEmpty()) value.put("link", firstURL);
            if (og.length != 0) {
                value.put("title", og[0]);
                value.put("image", og[1]);
            }
            target.sendMessage(new TextMessage(new Gson().toJson(value)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}