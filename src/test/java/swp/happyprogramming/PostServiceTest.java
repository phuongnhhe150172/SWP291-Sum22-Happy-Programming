package swp.happyprogramming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import swp.happyprogramming.domain.dto.PostDTO;
import swp.happyprogramming.adapter.port.out.IPostRepository;
import swp.happyprogramming.application.usecase.IPostService;

import java.util.List;

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
