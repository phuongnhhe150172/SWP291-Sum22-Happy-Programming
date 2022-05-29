package swp.happyprogramming.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.services.IMentorService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class MentorsManagementController {
    @Autowired
    private IMentorService mentorService;

    @GetMapping("/admin/mentors")
    public String mentors(Model model) {
        List<MentorDTO> mentors = mentorService.getMentors();
        model.addAttribute("mentors", mentors);
        return "admin/all-mentors";
    }
}
