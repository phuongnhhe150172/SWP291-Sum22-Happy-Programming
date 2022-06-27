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

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class SocketHandler extends TextWebSocketHandler {

    ArrayList<WebSocketSession> sessions = new ArrayList<>();
    Map<String, String> users = new HashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        Map value = new Gson().fromJson(message.getPayload(), Map.class);
        String senderId = (String) value.get("senderId");
        String receiverId = (String) value.get("receiverId");
        String content = (String) value.get("content");

        if (senderId != null) users.put(senderId, session.getId());
        if (content == null) return;

        Optional<WebSocketSession> target = sessions
                .stream()
                .filter(s -> s.getId().equals(users.get(receiverId)))
                .findFirst();

        if (target.isPresent()) target.get().sendMessage(new TextMessage(content));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }
}