package swp.happyprogramming.controllers.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import swp.happyprogramming.dto.PostDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Method;
import swp.happyprogramming.services.IMethodService;
import swp.happyprogramming.services.IPostService;
import swp.happyprogramming.services.IUserService;

import javax.servlet.http.HttpSession;
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

            return "redirect:update?id=" + postDTO.getId();
        }catch (NumberFormatException e){
            return "redirect:index";
        }
    }

    @GetMapping("/view/all")
    public String viewAllPost(Model model){
        List<PostDTO> listPostOngoing = postService.getListPostOngoing();
        Map<Long, List<UserDTO>> mapLikePost = postService.mapLikePost(listPostOngoing);

        model.addAttribute("listPost",listPostOngoing);
        model.addAttribute(("mapLikePost"),mapLikePost);
        return "/post/view/all";
    }
}
