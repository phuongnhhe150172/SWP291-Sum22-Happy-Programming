package swp.happyprogramming.controllers.mentor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.dto.MenteeDTO;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.services.IMenteeService;
import swp.happyprogramming.services.IMentorService;
import swp.happyprogramming.services.IUserService;

@Controller
public class MentorViewController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IMentorService mentorService;

    @Autowired
    private IMenteeService menteeService;

    @GetMapping("/mentor/view")
    public String viewMenteeProfile(Model model, @RequestParam(value = "idOr", required = false) String idOr,
                                    @RequestParam(value = "idEe", required = false) String idEe) {
        try {
            long mentorId = Integer.parseInt(idOr);
            long menteeId = Integer.parseInt(idEe);
            MentorDTO mentorDTO = mentorService.findMentor(mentorId);
            MenteeDTO menteeDTO = menteeService.findMentee(menteeId);
            Integer status = userService.statusRequest(mentorDTO.getId(), menteeDTO.getId());

            model.addAttribute("mentor", mentorDTO);
            model.addAttribute("mentee", menteeDTO);
            model.addAttribute("status", status);
            return "mentor/view/menteeProfile";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }
}
