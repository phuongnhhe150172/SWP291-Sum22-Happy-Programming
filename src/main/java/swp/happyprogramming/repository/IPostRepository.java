package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Post;
import swp.happyprogramming.model.User;

import java.util.List;

@Repository
public interface IPostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllByStatus(int status);

    @Query(value = "select u.* from user_like_posts as l inner join users as u on l.user_id = u.id where post_id = ?1",
    nativeQuery = true)
    List<User> findAllUserLikePost(long postId);
}
