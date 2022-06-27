package swp.happyprogramming.services;

import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.model.Message;

import java.nio.file.Path;
import java.util.List;

public interface IMessageService {
    List<Message> getMessagesByUserId(long id, long recId);

    void saveMessage(Message message);

    String uploadImage(Long sender_id, Long receiver_id, Path currentFolder, MultipartFile image);
}
