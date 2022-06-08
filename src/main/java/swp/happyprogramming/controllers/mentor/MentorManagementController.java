package swp.happyprogramming.controllers.mentor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.services.IMentorService;

import java.util.List;

@Controller
public class MentorManagementController {
    @Autowired
    private IMentorService mentorService;

    @GetMapping("/mentor/viewall")
    public String showMentor(Model model) {
        List<MentorDTO> mentorList = mentorService.getMentors();
        model.addAttribute("mentorList", mentorList);
        return "public/showMentor";
    }
}
