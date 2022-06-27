package swp.happyprogramming.controllers.mentor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.dto.ConnectDTO;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.User;
import swp.happyprogramming.repository.IUserRepository;
import swp.happyprogramming.services.IConnectService;
import swp.happyprogramming.services.IMentorService;
import swp.happyprogramming.services.IUserService;

import java.util.List;
import java.util.Map;

@Controller
public class MentorManagementController {
    @Autowired
    private IMentorService mentorService;

    @Autowired
    private IConnectService connectService;

    @Autowired
    private IUserService userService;

    @GetMapping("/mentor")
    public String showMentor(Model model, @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        if (email.equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(email);
        Pagination<MentorDTO> page = mentorService.getMentors(pageNumber);
        List<Long> connectedMentorIds = connectService.getConnectedMentor(user.getId());
        model.addAttribute("connections", connectedMentorIds);
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
