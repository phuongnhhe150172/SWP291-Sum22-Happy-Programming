package swp.happyprogramming.services;

import swp.happyprogramming.dto.PostDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Post;

import java.util.List;

public interface IPostService {
    List<Post> getAllPosts();

    PostDTO findPost(long id);

    void updatePost(PostDTO postDTO, long methodId, UserDTO userDTO);
}
