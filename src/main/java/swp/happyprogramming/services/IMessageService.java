package swp.happyprogramming.services;

import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.model.Message;

import java.nio.file.Path;
import java.util.List;

public interface IMessageService {
    List<Message> getMessagesByUserId(long id, long recId);

    Message saveMessage(Message message);
}
