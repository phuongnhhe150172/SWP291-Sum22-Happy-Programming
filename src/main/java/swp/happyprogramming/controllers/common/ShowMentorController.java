package swp.happyprogramming.controllers.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.services.IMentorService;

import java.util.List;

@Controller
public class ShowMentorController {
    @Autowired
    private IMentorService iMentorService;

    @GetMapping("/public/showmentor")
    public String showMentor(Model model){
        List<MentorDTO> mentorList = iMentorService.getMentors();
        model.addAttribute("mentorList", mentorList);
        return "public/showMentor";
    }
}
