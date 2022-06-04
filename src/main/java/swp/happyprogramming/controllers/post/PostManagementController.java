package swp.happyprogramming.controllers.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.dto.MenteeDTO;
import swp.happyprogramming.dto.MethodDTO;
import swp.happyprogramming.dto.PostDTO;
import swp.happyprogramming.services.IMenteeService;
import swp.happyprogramming.services.IMethodService;
import swp.happyprogramming.services.IPostService;

import java.util.List;

@Controller
@RequestMapping("/post")
public class PostManagementController {
    @Autowired
    private IPostService postService;

    @Autowired
    private IMenteeService menteeService;

    @Autowired
    private IMethodService methodService;

    @GetMapping("/update")
    public String updatePost(Model model, @RequestParam(value = "id",required = false) String id){
        try{
            long postId = Integer.parseInt(id);

            PostDTO postDTO = postService.findPost(postId);
            MenteeDTO menteeDTO = menteeService.findMentee(postDTO.getMenteeId());
            List<MethodDTO> listMethod = methodService.getAllMethod();

            model.addAttribute("post",postDTO);
            model.addAttribute("mentee",menteeDTO);
            model.addAttribute("listMethod",listMethod);
            return "/post/update";
        }catch (NumberFormatException e){
            return "redirect:/index";
        }
    }
}
