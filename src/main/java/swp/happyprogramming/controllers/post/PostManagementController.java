package swp.happyprogramming.controllers.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import swp.happyprogramming.dto.MethodDTO;
import swp.happyprogramming.dto.PostDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Method;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Post;
import swp.happyprogramming.model.User;
import swp.happyprogramming.model.Care;
import swp.happyprogramming.services.IMethodService;
import swp.happyprogramming.services.IPostService;
import swp.happyprogramming.services.IUserService;
import swp.happyprogramming.services.ICareService;
import swp.happyprogramming.services.servicesimpl.CareService;
import swp.happyprogramming.services.servicesimpl.MethodService;
import swp.happyprogramming.vo.PostVo;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/post")
public class PostManagementController {
    @Autowired
    private HttpSession session;

    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IMethodService methodService;

    @Autowired
    private ICareService careService;

    @GetMapping("/update")
    public String updatePost(Model model, @RequestParam(value = "id",required = false) String id){
        try{
            long postId = Integer.parseInt(id);

            PostDTO postDTO = postService.findPost(postId);
            UserDTO userDTO = userService.findUser(postDTO.getUser().getId());
            List<Method> listMethod = methodService.getAllMethod();

            model.addAttribute("post",postDTO);
            model.addAttribute("user",userDTO);
            model.addAttribute("listMethod",listMethod);
            return "/post/update";
        }catch (NumberFormatException e){
            return "redirect:/index";
        }
    }

    @PostMapping("/update")
    public String updatePost(@ModelAttribute("post") PostDTO postDTO,
                             @RequestParam Map<String, Object> params){
        try{
            long method = Integer.parseInt(String.valueOf(params.get("method")));
            UserDTO user =(UserDTO) session.getAttribute("userInformation");
            UserDTO userDTO = userService.findUser(user.getId());

            postService.updatePost(postDTO,method,userDTO);

            return "redirect:detail?id=" + postDTO.getId();
        }catch (NumberFormatException e){
            return "redirect:index";
        }
    }


    @RequestMapping(value="/care", method=RequestMethod.GET)
    public String CarePost(Model map, @RequestParam(value = "id",required = false) String id) {
        //map.addAttribute("foo", "bar");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        System.out.print("================================" + id + "======" + user.getId());
        Care care = new Care();
        care.setUserId(user.getId());
        care.setPostId(Long.parseLong(id));
        careService.save(care);
        return null;
    }

    @RequestMapping(value="/uncare", method=RequestMethod.GET)
    public String unCarePost(Model map, @RequestParam(value = "id",required = false) String id) {
        //map.addAttribute("foo", "bar");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        careService.deleteCare(user.getId(), Long.parseLong(id));
        return null;
    }

    @GetMapping("/delete")
    public String deletePost(Model model, @RequestParam(value = "postId",required = true) long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        List<PostDTO> listPostByUser = postService.getPostByUserId(user.getId());
        for (int i = listPostByUser.size()-1; i >= 0 ; i--){
            if (listPostByUser.get(i).getId() == postId) postService.deletePost(postId);
        }
        return "redirect:/post/created-post";
    }

    @GetMapping("/all")
    public String getPosts(Model model) {
        ArrayList<Post> posts = (ArrayList<Post>) postService.getAllPosts();
        ArrayList<PostVo> result = new ArrayList<>();

        for (Post p : posts) {
            PostDTO postDTO = postService.findPost(p.getId());
            UserDTO userDTO = postDTO.getUser();
            Method methodDTO = postDTO.getMethod();
            int liked  = careService.checkCared(userDTO.getId(), p.getId());
            PostVo pi = new PostVo(postDTO.getId(), userDTO.getImage(), userDTO.getFirstName() + userDTO.getLastName(), postDTO.getDescription(), postDTO.getStatus(), postDTO.getPrice(), methodDTO.getName());
            pi.setLiked(liked);
            result.add(pi);
        }
        model.addAttribute("posts", result);
        return "/admin/all-posts";
    }

    @GetMapping("/created-post")
    public String getCreatedPosts(Model model) {
        ArrayList<PostVo> result = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        List<PostDTO> listPostByUser = postService.getPostByUserId(user.getId());

        for (PostDTO postDTO : listPostByUser) {
            UserDTO userDTO = postDTO.getUser();
            Method methodDTO = postDTO.getMethod();
            PostVo pi = new PostVo(postDTO.getId(), userDTO.getImage(), userDTO.getFirstName() + userDTO.getLastName(), postDTO.getDescription(), postDTO.getStatus(), postDTO.getPrice(), methodDTO.getName());
            result.add(pi);
        }

        model.addAttribute("posts", result);
        return "/post/all-created-post";
    }

    @GetMapping("/create")
    public String createPosts(Model model) {

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.findByEmail(email);

            List<Method> listMethod = methodService.getAllMethod();
            model.addAttribute("user", user);
            model.addAttribute("listMethod",listMethod);
            return "/post/create";
        }catch (NumberFormatException e){
            return "redirect:/index";
        }
    }

    @PostMapping("/create")
    public String createPost(Model model,
                             @RequestParam Map<String, Object> params){
        try{
            UserDTO user =(UserDTO) session.getAttribute("userInformation");
            int method = Integer.parseInt(String.valueOf(params.get("method")));
            int status = Integer.parseInt(String.valueOf(params.get("status")));
            String content = String.valueOf(params.get("content"));
            float price = Float.parseFloat(String.valueOf(params.get("price")));
            postService.createNewPost(user, status, content, method, price);
            return "/post/all";
        }catch (NumberFormatException e){
            return "redirect:index";
        }
    }

    @GetMapping("/detail")
    public String detailPost(Model model, @RequestParam(value = "id",required = false) String id){
        try{
            long postId = Integer.parseInt(id);

            PostDTO postDTO = postService.findPost(postId);
            UserDTO userDTO = userService.findUser(postDTO.getUser().getId());
            List<Method> listMethod = methodService.getAllMethod();

            model.addAttribute("post",postDTO);
            model.addAttribute("user",userDTO);
            model.addAttribute("listMethod",listMethod);
            return "/post/detail";
        }catch (NumberFormatException e){
            return "redirect:index";
        }
    }
}
