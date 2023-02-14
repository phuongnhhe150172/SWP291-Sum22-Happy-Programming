package swp.happyprogramming.application.services;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.application.port.out.IMessageRepository;
import swp.happyprogramming.application.port.usecase.IMessageService;
import swp.happyprogramming.domain.model.Message;

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
}
