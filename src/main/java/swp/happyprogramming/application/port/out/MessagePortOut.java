package swp.happyprogramming.application.port.out;

import java.util.List;
import swp.happyprogramming.domain.model.Message;

public interface MessagePortOut {

  List<Message> getMessages(long id, long rec_id);

  Message save(Message message);
}