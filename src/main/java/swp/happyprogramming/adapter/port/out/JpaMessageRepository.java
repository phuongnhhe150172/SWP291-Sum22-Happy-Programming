package swp.happyprogramming.adapter.port.out;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMessageRepository extends JpaRepository<Message, Long> {

  @Query(value = "SELECT * FROM chat_messages WHERE sender_id = ?1 AND receiver_id = ?2", nativeQuery = true)
  List<Message> getMessages(long id, long rec_id);
}
