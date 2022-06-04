package swp.happyprogramming.controllers.mentee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import swp.happyprogramming.dto.MenteeDTO;
import swp.happyprogramming.services.IMenteeService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class MenteesManagementController {

    @Autowired
    private IMenteeService menteeService;

    @GetMapping("/mentees")
    public String mentors(Model model) {
        List<MenteeDTO> mentees = menteeService.getAllMentees();
        model.addAttribute("mentees", mentees);
        return "admin/all-mentees";
    }
}
