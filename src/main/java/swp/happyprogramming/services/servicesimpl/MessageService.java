package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.model.Message;
import swp.happyprogramming.repository.IMessageRepository;
import swp.happyprogramming.services.IMessageService;

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
        return messages1;
    }

    @Override
    public void saveMessage(Message message) {
        messageRepository.save(message);
    }
}
