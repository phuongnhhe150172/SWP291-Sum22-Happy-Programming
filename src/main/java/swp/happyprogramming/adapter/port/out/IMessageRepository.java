package swp.happyprogramming.adapter.port.out;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.application.port.out.MessagePortOut;
import swp.happyprogramming.domain.model.Message;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> ,
  MessagePortOut {

  @Query(value = "SELECT * FROM chat_messages WHERE sender_id = ?1 AND receiver_id = ?2", nativeQuery = true)
  List<Message> getMessages(long id, long rec_id);
}
