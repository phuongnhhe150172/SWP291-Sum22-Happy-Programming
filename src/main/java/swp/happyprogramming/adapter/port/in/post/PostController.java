package swp.happyprogramming.adapter.port.in.post;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.adapter.dto.PostDTO;
import swp.happyprogramming.adapter.dto.UserDTO;
import swp.happyprogramming.application.usecase.IPostService;
import swp.happyprogramming.application.usecase.IUserService;
import swp.happyprogramming.domain.model.Care;
import swp.happyprogramming.domain.model.Method;
import swp.happyprogramming.domain.model.Post;
import swp.happyprogramming.domain.model.User;
import swp.happyprogramming.domain.vo.PostVo;

@Controller
@RequestMapping("/post")
public class PostController {

  @Autowired
  private HttpSession session;

  @Autowired
  private IPostService postService;

  @Autowired
  private IUserService userService;

  @Secured("ROLE_MENTEE")
  @GetMapping("/update")
  public String updatePost(Model model, @RequestParam(value = "id", required = false) String id) {
    try {
      long postId = Integer.parseInt(id);

      PostDTO postDTO = postService.getPostById(postId);
      UserDTO userDTO = userService.findUser(postDTO.getUser().getId());
      List<Method> listMethod = userService.getAllMethod();

      model.addAttribute("post", postDTO);
      model.addAttribute("user", userDTO);
      model.addAttribute("listMethod", listMethod);
      return "/post/update";
    } catch (NumberFormatException e) {
      return "redirect:/index";
    }
  }

  @Secured("ROLE_MENTEE")
  @PostMapping("/update")
  public String updatePost(@ModelAttribute("post") PostDTO postDTO,
    @RequestParam Map<String, Object> params) {
    try {
      long method = Integer.parseInt(String.valueOf(params.get("method")));
      long useId = Integer.parseInt(String.valueOf(params.get("useId")));
      UserDTO userDTO = userService.findUser(useId);

      postService.updatePost(postDTO, method, userDTO);

      return "redirect:detail?id=" + postDTO.getId();
    } catch (NumberFormatException e) {
      return "redirect:index";
    }
  }


  @RequestMapping(value = "/care", method = RequestMethod.GET)
  public String CarePost(Model map, @RequestParam(value = "id", required = false) String id) {
    //map.addAttribute("foo", "bar");
    User user = getUser();
    System.out.print("================================" + id + "======" + user.getId());
    Care care = new Care();
    care.setUserId(user.getId());
    care.setPostId(Long.parseLong(id));
    userService.save(care);
    return null;
  }

  @RequestMapping(value = "/uncare", method = RequestMethod.GET)
  public String unCarePost(Model map, @RequestParam(value = "id", required = false) String id) {
    //map.addAttribute("foo", "bar");
    User user = getUser();

    userService.deleteCare(user.getId(), Long.parseLong(id));
    return null;
  }

  private User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    return userService.findByEmail(email);
  }

  @GetMapping("/delete")
  public String deletePost(Model model,
    @RequestParam(value = "postId") long postId) {
    User user = getUser();

    List<PostDTO> listPostByUser = postService.getPostByUserId(user.getId());
    for (int i = listPostByUser.size() - 1; i >= 0; i--) {
      if (listPostByUser.get(i).getId() == postId) {
        postService.deletePost(postId);
      }
    }
    return "redirect:/post/created-post";
  }

  @GetMapping("/all")
  public String getPosts(Model model) {
    ArrayList<Post> posts = (ArrayList<Post>) postService.getAllPosts();
    ArrayList<PostVo> result = new ArrayList<>();

    for (Post p : posts) {
      PostDTO postDTO = postService.getPostById(p.getId());
      UserDTO userDTO = postDTO.getUser();
      Method methodDTO = postDTO.getMethod();
      int liked = userService.checkCared(userDTO.getId(), p.getId());
      PostVo pi = new PostVo(postDTO.getId(), userDTO.getImage(),
        userDTO.getFirstName() + userDTO.getLastName(), postDTO.getDescription(),
        postDTO.getStatus(), postDTO.getPrice(), methodDTO.getName());
      pi.setLiked(liked);
      result.add(pi);
    }

    model.addAttribute("posts", result);
    return "/post/all-posts";
  }

  @GetMapping("/cared")
  public String getCaredUser(Model model, @RequestParam(value = "id") long id) {

    List<UserDTO> users = postService.getListUserLikePost(id);

    model.addAttribute("users", users);
    return "/post/cared-user";
  }

  @GetMapping("/created-post")
  public String getCreatedPosts(Model model) {
    ArrayList<PostVo> result = new ArrayList<>();

    User user = getUser();

    List<PostDTO> listPostByUser = postService.getPostByUserId(user.getId());

    for (PostDTO postDTO : listPostByUser) {
      UserDTO userDTO = postDTO.getUser();
      Method methodDTO = postDTO.getMethod();
      PostVo pi = new PostVo(postDTO.getId(), userDTO.getImage(),
        userDTO.getFirstName() + userDTO.getLastName(), postDTO.getDescription(),
        postDTO.getStatus(), postDTO.getPrice(), methodDTO.getName());
      result.add(pi);
    }

    model.addAttribute("posts", result);
    return "/post/all-created-post";
  }

  @GetMapping("/create")
  public String createPosts(Model model) {

    try {
      User user = getUser();
      List<Method> listMethod = userService.getAllMethod();
      model.addAttribute("user", user);
      model.addAttribute("listMethod", listMethod);
      return "/post/create";
    } catch (NumberFormatException e) {
      return "redirect:/index";
    }
  }

  @PostMapping("/create")
  public String createPost(Model model,
    @RequestParam Map<String, Object> params) {
    try {
      UserDTO user = (UserDTO) session.getAttribute("userInformation");
      int method = Integer.parseInt(String.valueOf(params.get("method")));
      int status = Integer.parseInt(String.valueOf(params.get("status")));
      String content = String.valueOf(params.get("content"));
      float price = Float.parseFloat(String.valueOf(params.get("price")));
      postService.createNewPost(user, status, content, method, price);
      return "redirect:/post/all";
    } catch (NumberFormatException e) {
      return "redirect:index";
    }
  }

  @GetMapping("/detail")
  public String detailPost(Model model, @RequestParam(value = "id", required = false) String id) {
    try {
      long postId = Integer.parseInt(id);

      PostDTO postDTO = postService.getPostById(postId);
      UserDTO userDTO = userService.findUser(postDTO.getUser().getId());
      List<Method> listMethod = userService.getAllMethod();

      model.addAttribute("post", postDTO);
      model.addAttribute("user", userDTO);
      model.addAttribute("listMethod", listMethod);
      return "/post/detail";
    } catch (NumberFormatException e) {
      return "redirect:index";
    }
  }
}
