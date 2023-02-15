package swp.happyprogramming.application.port.out;

import java.util.List;
import swp.happyprogramming.domain.model.Care;


public interface CarePortOut {

  void deleteByUserIdAndPostId(long userId, long post_id);

  List<Care> findUserLikePost(long postId, long user_id);

  Care save(Care care);
}
