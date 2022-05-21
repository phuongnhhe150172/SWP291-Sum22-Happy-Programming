package swp.happyprogramming.controllers.mentor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.services.servicesimpl.MentorService;

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

    @GetMapping("/mentor/profile/update")
    public String updateProfileMentor(Model model, @RequestParam(value = "id", required = false) String id) {
        try {
            long mentorId = Integer.parseInt(id);
            System.out.println(mentorId);
            MentorDTO mentorDTO = mentorService.findMentor(mentorId);
            model.addAttribute("mentor", mentorDTO);
            return "mentor/profile/update";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }
}