package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.PostDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Method;
import swp.happyprogramming.model.Post;
import swp.happyprogramming.model.User;
import swp.happyprogramming.repository.IMethodRepository;
import swp.happyprogramming.repository.IPostRepository;
import swp.happyprogramming.repository.IUserRepository;
import swp.happyprogramming.services.IPostService;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IMethodRepository methodRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    ModelMapper mapper = new ModelMapper();

    @Override
    public PostDTO findPost(long id) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            PostDTO postDTO = mapper.map(post, PostDTO.class);
            return postDTO;
        }
        return null;
    }

    @Override
    public void updatePost(PostDTO postDTO, long methodId, UserDTO userDTO) {
        Optional<Post> optionalPost = postRepository.findById(postDTO.getId());
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            User user = mapper.map(userDTO, User.class);
            updateInformationPost(postDTO,methodId,user);
        }
    }

    private void updateInformationPost(PostDTO postDTO, long methodId, User user){
//        post.setDescription(postDTO.getDescription());
//        post.setPrice(postDTO.getPrice());
//        post.setStatus(postDTO.getStatus());
//        post.setMethodId(method);
        Post postMap = mapper.map(postDTO, Post.class);
        postMap.setModified(Date.from(Instant.now()));
        Method method = methodRepository.findById(methodId);
        postMap.setMethod(method);
        postMap.setUser(user);
        postRepository.save(postMap);
    }
}
