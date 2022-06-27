package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.happyprogramming.model.Message;

import java.util.List;

public interface IMessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT * FROM chat_messages WHERE sender_id = ?1 AND receiver_id = ?2", nativeQuery = true)
    List<Message> getMessages(long id, long rec_id);
}
