package swp.happyprogramming.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swp.happyprogramming.model.Post;
import swp.happyprogramming.services.IPostService;
import swp.happyprogramming.vo.PostVo;

import swp.happyprogramming.dto.PostDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Method;

import java.util.ArrayList;

@Controller
public class PostController {

    @Autowired
    private IPostService postService;

    
}
