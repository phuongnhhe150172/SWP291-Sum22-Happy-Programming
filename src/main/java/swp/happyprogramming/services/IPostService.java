package swp.happyprogramming.services;

import swp.happyprogramming.dto.PostDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Post;

import java.util.List;
import java.util.Map;

public interface IPostService {
    List<Post> getAllPosts();

    PostDTO findPost(long id);

    void updatePost(PostDTO postDTO, long methodId, UserDTO userDTO);

    List<PostDTO> getListPostOngoing();

    List<UserDTO> getListUserLikePost(long postId);

    Map<Long,List<UserDTO>> mapLikePost(List<PostDTO> listPost);
    
    List<PostDTO> getPostByUserId(long userId);

    PostDTO getPostById(long postId);

    void deletePost(long postId);

    Pagination<PostDTO> getPostsPaging(int pageNumber);

    void createNewPost(UserDTO userDTO, int statusId,String content, long methodId, float price);

}
