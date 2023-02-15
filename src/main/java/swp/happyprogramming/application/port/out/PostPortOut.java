package swp.happyprogramming.application.port.out;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import swp.happyprogramming.domain.model.Post;
import swp.happyprogramming.domain.model.User;

public interface PostPortOut {

  List<Post> findAllByStatus(int status);

  Page<Post> findAllByStatusPaging(Pageable pageable, int status);

  List<Long> findAllUserLikePost(long postId);

  List<User> findAllUserNameLikePost(long postId);

  Optional<Post> findById(long postId);

  Post save(Post p);

  List<Post> findAll();

  void deleteById(long postId);
}