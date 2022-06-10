package swp.happyprogramming.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.services.ISkillService;
import swp.happyprogramming.services.IUserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IUserService userService;

    @Autowired
    private ISkillService skillService;

    @GetMapping("/dashboard")
    public String displayDashboardAdmin(Model model){
        int totalNumberOfMentors = userService.countUsersByRolesLike("ROLE_MENTOR");
        int totalNumberOfMentees = userService.countUsersByRolesLike("ROLE_MENTEE");

        model.addAttribute("totalNumberOfMentors", totalNumberOfMentors);
        model.addAttribute("totalNumberOfMentees", totalNumberOfMentees);
        model.addAttribute("totalNumberOfRequests", 123);

        return "admin/admin_dashboard";
    }

    @GetMapping("/mentees")
    public String showAllMentees(Model model){
        List<UserDTO> mentees =  userService.findAllMentees();
        model.addAttribute("mentees", mentees);
        return "admin/all-mentees";
    }

    @GetMapping("/mentee")
    public String showMentee(Model model, @RequestParam(value = "id", required = false) long menteeId){
        UserDTO mentee = userService.findUser(menteeId);
        model.addAttribute("mentee", mentee);
        return "admin/view-mentee";
    }

    @GetMapping("/delete")
    public String deleteMentee(@RequestParam(value = "id", required = false) long menteeId){
        userService.removeMentee(menteeId);
        return "redirect:/admin/mentees";
    }

    @GetMapping("/skills")
    public String getAllSkill(Model model){
        List<Skill> skillList = skillService.getAllSkill();
        model.addAttribute("skills", skillList);
        return "admin/all-skills";
    }

    @GetMapping("/create-skill")
    public String createSkill(){
        return "create-skill";
    }


    @PostMapping("/create-skill")
    public String createSkill(@RequestParam String skillName){
        Skill skill = new Skill();
        skill.setName(skillName);
        skillService.save(skill);
        return "redirect:/admin/skills";
    }
}
