package swp.happyprogramming.adapter.port.in.mentor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.domain.dto.UserAvatarDTO;
import swp.happyprogramming.domain.dto.MentorDTO;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.User;
import swp.happyprogramming.application.usecase.IConnectService;
import swp.happyprogramming.application.usecase.IMentorService;
import swp.happyprogramming.application.usecase.IRequestService;
import swp.happyprogramming.application.usecase.IUserService;

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

    @Autowired
    private IRequestService requestService;

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
        List<Long> requestedMentorIds = requestService.getRequestedMentorId(user.getId());

        model.addAttribute("connections", connectedMentorIds);
        model.addAttribute("requests", requestedMentorIds);
        model.addAttribute("mentorList", page.getPaginatedList());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", page.getPageNumbers().size());
        return "mentor/showMentor";
    }

    @PostMapping("/mentor")
    public String showMentor(Model model, @RequestParam Map<String, Object> params) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (email.equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(email);
        List<MentorDTO> page = mentorService.filterMentors(String.valueOf(params.get("search")));
        List<Long> connectedMentorIds = connectService.getConnectedMentor(user.getId());
        List<Long> requestedMentorIds = requestService.getRequestedMentorId(user.getId());

        model.addAttribute("connections", connectedMentorIds);
        model.addAttribute("requests", requestedMentorIds);
        model.addAttribute("mentorList", page);
        model.addAttribute("search", String.valueOf(params.get("search")));
        return "mentor/filterShowMentor";
    }

    @GetMapping("/mentor/top")
    public String topMentor(Model model) {
        List<UserAvatarDTO> mentorList = mentorService.getTopMentors();
        model.addAttribute("mentorList", mentorList);
        return "mentor/suggestions";
    }
}
