package swp.happyprogramming.controllers.mentor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.dto.ProvinceDTO;
import swp.happyprogramming.services.servicesimpl.MentorService;
import swp.happyprogramming.services.servicesimpl.ProvinceService;

import java.util.List;

@Controller
public class MentorProfileController {
    @Autowired
    private MentorService mentorService;

    @Autowired
    private ProvinceService provinceService;

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
            MentorDTO mentorDTO = mentorService.findMentor(mentorId);
            List<ProvinceDTO> listProvinces = provinceService.findAllProvinces();
            model.addAttribute("mentor", mentorDTO);
            model.addAttribute("listProvinces", listProvinces);
            return "mentor/profile/update";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }
}