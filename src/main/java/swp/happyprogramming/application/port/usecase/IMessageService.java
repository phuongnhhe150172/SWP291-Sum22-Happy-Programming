package swp.happyprogramming.application.port.usecase;

import java.util.List;
import swp.happyprogramming.domain.model.Message;

public interface IMessageService {

  List<Message> getMessagesByUserId(long id, long recId);

  Message saveMessage(Message message);
}
