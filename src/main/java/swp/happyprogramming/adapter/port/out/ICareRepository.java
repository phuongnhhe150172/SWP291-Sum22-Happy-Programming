package swp.happyprogramming.adapter.port.out;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.application.port.out.CarePortOut;
import swp.happyprogramming.domain.model.Care;


@Repository
public interface ICareRepository extends JpaRepository<Care, Long> ,
  CarePortOut {

  @Modifying
  @Transactional
  @Query(value = "delete from user_like_posts where user_id = ?1 and post_id = ?2", nativeQuery = true)
  void deleteByUserIdAndPostId(long userId, long post_id);

  @Query(value = "select * from user_like_posts where post_id = ?1 and user_id = ?2", nativeQuery = true)
  List<Care> findUserLikePost(long postId, long user_id);
}
