package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.model.Message;
import swp.happyprogramming.repository.IMessageRepository;
import swp.happyprogramming.services.IMessageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageService implements IMessageService {

    @Autowired
    private IMessageRepository messageRepository;

    @Override
    public List<Message> getMessagesByUserId(long id, long recId) {
        List<Message> messages1 = messageRepository.getMessages(id, recId);
        List<Message> messages2 = messageRepository.getMessages(recId, id);
        messages1.addAll(messages2);
        messages1.sort(Comparator.comparing(Message::getTimestamp));
        return messages1;
    }

    @Override
    public Message saveMessage(Message message) {
        message.setTimestamp(Instant.now());
        return messageRepository.save(message);
    }

    @Override
    public String uploadImage(Long sender_id, Long receiver_id, Path currentFolder, MultipartFile image) {
        Path imagesPath = Paths.get("src/main/upload/static/imgs");
        String imageName = sender_id + "_" + receiver_id + "_" + Instant.now().getEpochSecond() + ".jpg";
        Path imagePath = Paths.get(imageName);
        Path file = currentFolder.resolve(imagesPath).resolve(imagePath);
        try {
            Files.write(file, image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/upload/static/imgs/" + imageName;
    }
}
