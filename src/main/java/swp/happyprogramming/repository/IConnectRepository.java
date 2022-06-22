package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Connect;

import java.util.Optional;

@Repository
public interface IConnectRepository extends JpaRepository<Connect,Long> {
    @Query(value = "select * from connections where user1 = ?1 and user2 = ?2", nativeQuery = true)
    Optional<Connect> findConnectByUser1IdAndUser2Id(long user1Id, long user2Id);
}
