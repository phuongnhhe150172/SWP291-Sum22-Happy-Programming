package swp.happyprogramming.application.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import swp.happyprogramming.adapter.dto.PostDTO;
import swp.happyprogramming.adapter.dto.UserDTO;
import swp.happyprogramming.application.port.out.MethodPortOut;
import swp.happyprogramming.application.port.out.PostPortOut;
import swp.happyprogramming.application.port.out.UserPortOut;
import swp.happyprogramming.application.port.usecase.IPostService;
import swp.happyprogramming.domain.model.Method;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Post;
import swp.happyprogramming.domain.model.User;
import swp.happyprogramming.utility.Utility;

@Service
public class PostService implements IPostService {

  ModelMapper mapper = new ModelMapper();
  @Autowired
  private PostPortOut postRepository;
  @Autowired
  private MethodPortOut methodRepository;
  @Autowired
  private UserPortOut userRepository;

  @Override
  public List<Post> getAllPosts() {
    return postRepository.findAll();
  }

  @Override
  public PostDTO getPostById(long postId) {
    Optional<Post> post = postRepository.findById(postId);
    return mapPost(post.orElse(null));
  }

  @Override
  public void updatePost(PostDTO postDTO, long methodId, UserDTO userDTO) {
    Optional<Post> optionalPost = postRepository.findById(postDTO.getId());
    if (optionalPost.isPresent()) {
      User user = mapper.map(userDTO, User.class);
      updateInformationPost(postDTO, methodId, user);
    }
  }

  private void updateInformationPost(PostDTO postDTO, long methodId,
    User user) {
    Post postMap = mapper.map(postDTO, Post.class);
    Method method = methodRepository.findById(methodId);
    postMap.setMethod(method);
    postMap.setUser(user);
    postRepository.save(postMap);
  }

  @Override
  public void createNewPost(UserDTO userDTO, int statusId, String content,
    long methodId,
    float price) {
    User user = mapper.map(userDTO, User.class);
    Method method = methodRepository.findById(methodId);
    Post p = new Post();
    p.setMethod(method);
    p.setPrice(price);
    p.setUser(user);
    p.setStatus(statusId);
    p.setDescription(content);

    postRepository.save(p);
  }

  @Override
  public List<PostDTO> getListPostOngoing() {
    List<Post> listPost = postRepository.findAllByStatus(2);
    List<PostDTO> listPostDTO = listPost.stream()
      .map(value -> mapper.map(value, PostDTO.class))
      .collect(Collectors.toList());
    for (int i = 0; i < listPost.size(); i++) {
      listPostDTO.get(i).setUser(Utility.mapUser(listPost.get(i).getUser()));
    }
    return listPostDTO;
  }

  @Override
  public List<UserDTO> getListUserLikePost(long postId) {
    List<Long> listUserId = postRepository.findAllUserLikePost(postId);
    List<User> listUser = listUserId.stream()
      .map(id -> userRepository.findById(id).orElse(null))
      .toList();
    return Utility.mapList(listUser, Utility::mapUser);
  }

  @Override
  public Map<Long, List<UserDTO>> mapLikePost(List<PostDTO> listPost) {
    Map<Long, List<UserDTO>> mapLikePost = new HashMap<>();
    for (PostDTO postDTO : listPost) {
      mapLikePost.put(postDTO.getId(), getListUserLikePost(postDTO.getId()));
    }
    return mapLikePost;
  }

  @Override
  public List<PostDTO> getPostByUserId(long userId) {
    List<Post> posts = postRepository.findAll();
    return posts.stream()
      .filter(post -> post.getUser().getId() == userId)
      .map(this::mapPost)
      .toList();
  }

  @Override
  public void deletePost(long postId) {
    postRepository.deleteById(postId);
  }

  @Override
  public Pagination<PostDTO> getPostsPaging(int pageNumber) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10);
    Page<Post> page = postRepository.findAllByStatusPaging(pageRequest, 2);
    return Utility.getPagination(page, this::mapPost);
  }

  private PostDTO mapPost(Post post) {
    if (post == null) {
      return null;
    }
    PostDTO postDTO = mapper.map(post, PostDTO.class);
    postDTO.setUser(Utility.mapUser(post.getUser()));
    return postDTO;
  }
}
