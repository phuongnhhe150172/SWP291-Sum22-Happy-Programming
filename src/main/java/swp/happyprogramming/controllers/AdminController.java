package swp.happyprogramming.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import swp.happyprogramming.dto.*;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Request;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.services.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IUserService userService;

    @Autowired
    private ISkillService skillService;

    @Autowired
    private IMentorService mentorService;

    @Autowired
    private IRequestService requestService;

    @Autowired
    private IPostService postService;

    @Autowired
    private IConnectService connectService;


    @GetMapping("/dashboard")
    public String displayDashboardAdmin(Model model) {
        int totalNumberOfMentors = userService.countUsersByRolesLike("ROLE_MENTOR");
        int totalNumberOfMentees = userService.countUsersByRolesLike("ROLE_MENTEE");
        long totalNumberOfRequests = requestService.countTotalRequest();
        long MonthlyRequest = requestService.countMonthlyRequest();

        model.addAttribute("totalNumberOfMentors", totalNumberOfMentors);
        model.addAttribute("totalNumberOfMentees", totalNumberOfMentees);
        model.addAttribute("totalNumberOfRequests", totalNumberOfRequests);

        return "admin/admin_dashboard";
    }

    @GetMapping("/mentees")
    public String showAllMentees(
            Model model,
            @RequestParam Map<String, String> params,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber
    ) {
        String rawFirstName = params.get("first_name");
        String rawLastName = params.get("last_name");
        String rawPhone = params.get("phone");
        String rawEmail = params.get("email");
        String firstName = (rawFirstName == null || rawFirstName.trim() == "") ? null : rawFirstName;
        String lastName = (rawLastName == null || rawLastName.trim() == "") ? null : rawLastName;
        String phone = (rawPhone == null || rawPhone.trim() == "") ? null : rawPhone;
        String email = (rawEmail == null || rawEmail.trim() == "") ? null : rawEmail;

        Pagination<UserDTO> page = userService.getMentees(pageNumber, firstName, lastName, phone, email);
        model.addAttribute("mentees", page.getPaginatedList());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", page.getPageNumbers().size());
        model.addAttribute("first_name", firstName);
        model.addAttribute("last_name", lastName);
        model.addAttribute("phone", phone);
        model.addAttribute("email", email);
        return "admin/all-mentees";
    }

    @GetMapping("/mentors")
    public String showMentors(Model model, @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber) {
        //        Nguyễn Huy Hoàng - 46 - List all mentors (admin)
        Pagination<MentorDTO> page = mentorService.getMentors(pageNumber);
        model.addAttribute("mentors", page.getPaginatedList());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", page.getPaginatedList().size());
        return "admin/all-mentors";
    }

    @GetMapping("/mentor")
    public String viewMentor(Model model, @RequestParam(value = "id", required = false) long mentorId) {
        MentorDTO mentorDTO = mentorService.findMentor(mentorId);
        model.addAttribute("mentor", mentorDTO);
        return "admin/view-mentor";
    }

    @GetMapping("/mentee")
    public String showMentee(Model model, @RequestParam(value = "id", required = false) long menteeId) {
        UserDTO mentee = userService.findUser(menteeId);
        model.addAttribute("mentee", mentee);
        return "admin/view-mentee";
    }

    @GetMapping("/delete")
    public String deleteMentee(@RequestParam(value = "id", required = false) long menteeId) {
        userService.removeMentee(menteeId);
        return "redirect:/admin/mentees";
    }

    @GetMapping("/skills")
    public String getAllSkill(Model model, @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        Pagination<Skill> skills = skillService.getAllSkill(pageNumber);
        model.addAttribute("skills", skills.getPaginatedList());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", skills.getPageNumbers().size());
        return "admin/all-skills";
    }

    @GetMapping("/create-skill")
    public String createSkill() {
        return "admin/create-skill";
    }


    @PostMapping("/create-skill")
    public String createSkill(@RequestParam String skillName) {
        Skill skill = new Skill();
        skill.setName(skillName);
        skillService.save(skill);
        return "redirect:/admin/skills";
    }

    @GetMapping("/update-skill")
    public String showSkillToUpdate(Model model, @RequestParam(value = "id", required = false) long id) {
        Skill skill = skillService.findSkillById(id);
        model.addAttribute("skill", skill);
        return "admin/update-skill";
    }

    @PostMapping("/update-skill")
    public String updateSkill(@RequestParam String skillId, @RequestParam String skillName) {
        Skill skill = new Skill();
        long id = Integer.parseInt(String.valueOf(skillId));
        skill.setId(id);
        skill.setName(skillName);
        skillService.save(skill);
        return "redirect:/admin/skills";
    }


    @GetMapping("/requests")
    public String showAllRequests(Model model, @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        // Trinh Trung Kien - 52 - View all requests (admin)
        Pagination<Request> requests = requestService.getAllRequest(pageNumber);
        model.addAttribute("requests", requests.getPaginatedList());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", requests.getPageNumbers().size());
        return "admin/all-requests";
    }

    @GetMapping("/posts")
    public String viewAllPost(Model model, @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber) {
        Pagination<PostDTO> page = postService.getPostsPaging(pageNumber);
        Map<Long, List<UserDTO>> mapLikePost = postService.mapLikePost(page.getPaginatedList());

        model.addAttribute("listPost", page.getPaginatedList());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("mapLikePost", mapLikePost);
        model.addAttribute("totalPages", page.getPageNumbers().size());
        return "/admin/all-posts";
    }

    @GetMapping("/connections")
    public String viewAllConn(Model model, @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        Pagination<ConnectDTO> connects = connectService.findAllConnections(pageNumber);
        model.addAttribute("connections", connects.getPaginatedList());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", connects.getPageNumbers().size());
        return "admin/all-connections";
    }

    @GetMapping("/enable")
    public String enableUser(@RequestParam(value = "id", required = false) int id) {
        userService.enableUser(id);
        return "redirect:mentors";
    }

    @GetMapping("/disable")
    public String disableUser(@RequestParam(value = "id", required = false) int id) {
        userService.disableUser(id);
        return "redirect:mentors";
    }

}
