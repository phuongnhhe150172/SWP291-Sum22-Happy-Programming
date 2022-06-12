package swp.happyprogramming.controllers.skills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.services.ISkillService;
import swp.happyprogramming.services.servicesimpl.SkillService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class SkillsManagementController {
    @Autowired
    private ISkillService skillService;

    @GetMapping("/skills")
    public String getAllSkill(Model model){
        List<Skill> skillList = skillService.getAllSkill();
        model.addAttribute("skillList", skillList);
        return "skills/all-skills";
    }

    @GetMapping("/createskill")
    public String createSkill(){
        return "admin/skillCreate";
    }


    @PostMapping("/createskill")
    public @ResponseBody
    String createSkill(@RequestParam String skillName){
        Skill skill = new Skill();

        skill.setName(skillName);
        skillService.save(skill);
        return "Saved successfully!";
    }


}
