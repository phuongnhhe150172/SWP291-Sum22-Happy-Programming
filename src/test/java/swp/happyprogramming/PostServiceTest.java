package swp.happyprogramming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import swp.happyprogramming.dto.PostDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Post;
import swp.happyprogramming.model.User;
import swp.happyprogramming.repository.IPostRepository;
import swp.happyprogramming.services.IPostService;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PostServiceTest {
    @Autowired
    private IPostService postService;

    @Autowired
    private IPostRepository postRepository;

    @Test
    public void testGetPostByUserId(){
        long userId = 24;
        List<PostDTO> postDTOList = postService.getPostByUserId(userId);
        long postId = 1;
        PostDTO postDTO = postService.getPostById(postId);

    }
}
