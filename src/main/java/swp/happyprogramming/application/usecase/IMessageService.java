package swp.happyprogramming.application.usecase;

import swp.happyprogramming.domain.model.Message;

import java.util.List;

public interface IMessageService {
    List<Message> getMessagesByUserId(long id, long recId);

    Message saveMessage(Message message);
}
