package swp.happyprogramming.adapter.port.out;

import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.application.port.out.ConnectPortOut;
import swp.happyprogramming.domain.model.Connect;

@Repository
public interface IConnectRepository extends JpaRepository<Connect, Long> ,
  ConnectPortOut {

  @Query(value = "select * from connections where user1 = ?1 and user2 = ?2", nativeQuery = true)
  Optional<Connect> findConnectByUser1IdAndUser2Id(long user1Id, long user2Id);

  @Modifying
  @Transactional
  @Query(value = "delete from connections where user1 = ?1 and user2 = ?2 ", nativeQuery = true)
  void deleteConnection(long user1, long user2);
}
