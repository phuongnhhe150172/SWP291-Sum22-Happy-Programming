package swp.happyprogramming.services;

import swp.happyprogramming.model.Message;

import java.util.List;

public interface IMessageService {
    List<Message> getMessagesByUserId(long id, long recId);

    Message saveMessage(Message message);
}
