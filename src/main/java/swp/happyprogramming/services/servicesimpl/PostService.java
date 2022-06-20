package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.dto.PostDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Method;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Post;
import swp.happyprogramming.model.User;
import swp.happyprogramming.repository.IMethodRepository;
import swp.happyprogramming.repository.IPostRepository;
import swp.happyprogramming.repository.IUserRepository;
import swp.happyprogramming.services.IPostService;
import swp.happyprogramming.utility.Utility;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Override
    public List<PostDTO> getListPostOngoing(){
        List<Post> listPost = postRepository.findAllByStatus(2);
        List<PostDTO> listPostDTO = listPost.stream().map(value -> mapper.map(value, PostDTO.class)).collect(Collectors.toList());
        for (int i = 0; i< listPost.size() ; i++){
            listPostDTO.get(i).setUser(Utility.mapUser(listPost.get(i).getUser()));
        }
        return listPostDTO;
    }

    @Override
    public List<UserDTO> getListUserLikePost(long postId){
        List<Long> listUserId = postRepository.findAllUserLikePost(postId);
        List<User> listUser = listUserId.stream().map(value -> userRepository.findById(value).orElse(null)).collect(Collectors.toList());
        List<UserDTO> listUserDTO = listUser.stream().map(value -> Utility.mapUser(value)).collect(Collectors.toList());
        return listUserDTO;
    }

    @Override
    public Map<Long,List<UserDTO>> mapLikePost(List<PostDTO> listPost){
        Map<Long,List<UserDTO>> mapLikePost = new HashMap<>();
        for (PostDTO postDTO : listPost) {
            mapLikePost.put(postDTO.getId(),getListUserLikePost(postDTO.getId()));
        }
        return mapLikePost;
    }

    @Override
    public List<PostDTO> getPostByUserId(long userId) {
        List<Post> posts = postRepository.findAll();
        List<Post> postByUser = new ArrayList<>();
        for (Post post :posts) {
            if (post.getUser().getId() == userId){
                postByUser.add(post);
            }
        }
        List<PostDTO> listPostDTO = postByUser.stream().map(value -> mapper.map(value, PostDTO.class)).collect(Collectors.toList());
        for (int i = 0; i< postByUser.size() ; i++){
            listPostDTO.get(i).setUser(Utility.mapUser(postByUser.get(i).getUser()));
        }
        return listPostDTO;
    }

    @Override
    public PostDTO getPostById(long postId) {
        Optional<Post> post = postRepository.findById(postId);
        PostDTO postDTO = mapper.map(post.get(), PostDTO.class);
        postDTO.setUser(Utility.mapUser(post.get().getUser()));
        return postDTO;
    }

    @Override
    public void deletePost(long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Pagination<PostDTO> getPostsPaging(int pageNumber){
        PageRequest pageRequest = PageRequest.of(pageNumber - 1,10);
        Page<Post> page = postRepository.findAllByStatusPaging(pageRequest, 2);
        int totalPages = page.getTotalPages();
        List<Post> posts = page.getContent();
        List<PostDTO> postDTOS = posts
                .stream()
                .map(value -> mapper.map(value,PostDTO.class))
                .collect(Collectors.toList());
        for (int i = 0; i< posts.size() ; i++){
            postDTOS.get(i).setUser(Utility.mapUser(posts.get(i).getUser()));
        }
        List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
        return new Pagination<>(postDTOS,pageNumbers);
    }
}
