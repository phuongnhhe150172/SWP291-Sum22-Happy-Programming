package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.PostDTO;
import swp.happyprogramming.model.Post;
import swp.happyprogramming.repository.IPostRepository;
import swp.happyprogramming.services.IPostService;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class PostService implements IPostService {
    @Autowired
    private IPostRepository postRepository;

    @Override
    public PostDTO findPost(long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        ModelMapper mapper = new ModelMapper();
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            PostDTO postDTO = mapper.map(post, PostDTO.class);
            return postDTO;
        }
        return null;
    }

    @Override
    public void updatePost(PostDTO postDTO,long postId, long method) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            updateInformationPost(postDTO,post,method);
        }
    }

    private void updateInformationPost(PostDTO postDTO, Post post, long method){
        post.setDescription(postDTO.getDescription());
        post.setPrice(postDTO.getPrice());
        post.setStatus(postDTO.getStatus());
        post.setMethodId(method);
        post.setModified(Date.from(Instant.now()));
        postRepository.save(post);
    }

}
