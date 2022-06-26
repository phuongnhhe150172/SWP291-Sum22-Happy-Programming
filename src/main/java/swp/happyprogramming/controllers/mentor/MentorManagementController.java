package swp.happyprogramming.controllers.mentor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.services.IMentorService;

import java.util.List;
import java.util.Map;

@Controller
public class MentorManagementController {
    @Autowired
    private IMentorService mentorService;

    @GetMapping("/mentor")
    public String showMentor(Model model, @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber) {
        Pagination<MentorDTO> page = mentorService.getMentors(pageNumber);
        model.addAttribute("mentorList", page.getPaginatedList());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", page.getPageNumbers().size());
        return "mentor/showMentor";
    }

    @GetMapping("/mentor/search")
    public String searchMentor(Model model, @RequestParam Map<String, Object> params) {
        List<MentorDTO> mentorList = mentorService.searchMentors(params);
        model.addAttribute("mentorList", mentorList);
        return "mentor/search";
    }

    @GetMapping("/mentor/top")
    public String topMentor(Model model) {
        List<MentorDTO> mentorList = mentorService.getTopMentors();
        model.addAttribute("mentorList", mentorList);
        return "mentor/suggestions";
    }
}
