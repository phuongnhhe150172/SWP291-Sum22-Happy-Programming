package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.model.Post;
import swp.happyprogramming.repository.IPostRepository;
import swp.happyprogramming.services.IPostService;

import java.util.List;

@Service
public class PostService implements IPostService {

    @Autowired
    private IPostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
