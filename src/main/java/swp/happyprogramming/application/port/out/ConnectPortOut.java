package swp.happyprogramming.application.port.out;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import swp.happyprogramming.domain.model.Connect;


public interface ConnectPortOut {

  Optional<Connect> findConnectByUser1IdAndUser2Id(long user1Id, long user2Id);

  void deleteConnection(long user1, long user2);

  Connect save(Connect connect);

  List<Connect> findAll();

  Page<Connect> findAll(PageRequest pageRequest);
}
