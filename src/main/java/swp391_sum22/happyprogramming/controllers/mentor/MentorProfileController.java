package swp391_sum22.happyprogramming.controllers.mentor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.WebRequest;
import swp391_sum22.happyprogramming.dto.MentorDTO;
import swp391_sum22.happyprogramming.services.servicesimpl.MentorService;

@Controller
public class MentorProfileController {
    @Autowired
    private MentorService mentorService;

    @GetMapping("/mentor/profile/{id}")
    public String getProfile(WebRequest request, Model model, @PathVariable String id) {
        try {
            long mentorId = Integer.parseInt(id);
            MentorDTO mentorDTO = mentorService.findMentor(mentorId);
            model.addAttribute("mentor", mentorDTO);
            return "profile";
        } catch (NumberFormatException e) {
            return "redirect:/index";
        }
    }
}