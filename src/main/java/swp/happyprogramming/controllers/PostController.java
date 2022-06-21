package swp.happyprogramming.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swp.happyprogramming.model.Post;
import swp.happyprogramming.services.IPostService;

import java.util.ArrayList;

@Controller
public class PostController {

    @Autowired
    private IPostService postService;

    @GetMapping("/posts")
    public String getPosts(Model model) {
        ArrayList<Post> posts = (ArrayList<Post>) postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "/admin/all-posts";
    }
}
