package swp.happyprogramming.application.port.usecase;

import java.util.List;
import java.util.Map;
import swp.happyprogramming.adapter.dto.PostDTO;
import swp.happyprogramming.adapter.dto.UserDTO;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Post;

public interface IPostService {

  List<Post> getAllPosts();

  void updatePost(PostDTO postDTO, long methodId, UserDTO userDTO);

  List<PostDTO> getListPostOngoing();

  List<UserDTO> getListUserLikePost(long postId);

  Map<Long, List<UserDTO>> mapLikePost(List<PostDTO> listPost);

  List<PostDTO> getPostByUserId(long userId);

  PostDTO getPostById(long postId);

  void deletePost(long postId);

  Pagination<PostDTO> getPostsPaging(int pageNumber);

  void createNewPost(UserDTO userDTO, int statusId, String content,
    long methodId, float price);

}