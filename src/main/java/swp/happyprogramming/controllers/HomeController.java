package swp.happyprogramming.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.model.User;

import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String getHomePage(){
        return "index";
    }

}
