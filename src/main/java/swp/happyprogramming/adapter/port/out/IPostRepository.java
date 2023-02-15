package swp.happyprogramming.adapter.port.out;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.application.port.out.PostPortOut;
import swp.happyprogramming.domain.model.Post;
import swp.happyprogramming.domain.model.User;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> ,
  PostPortOut {

  List<Post> findAllByStatus(int status);

  @Query(value = "select * from posts where status = ?1", nativeQuery = true)
  Page<Post> findAllByStatusPaging(Pageable pageable, int status);

  @Query(value = "select id from users where id in (select user_id from user_like_posts where post_id = ?1)",
    nativeQuery = true)
  List<Long> findAllUserLikePost(long postId);

  @Query(value = "select users.* from users where id in (select user_id from user_like_posts where post_id = ?1)",
    nativeQuery = true)
  List<User> findAllUserNameLikePost(long postId);
}
