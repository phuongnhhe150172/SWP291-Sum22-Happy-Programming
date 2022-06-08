package swp.happyprogramming.controllers.skills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.services.ISkillService;
import swp.happyprogramming.services.servicesimpl.SkillService;

import java.util.List;

@Controller
public class SkillsManagementController {
    @Autowired
    private ISkillService skillService;

    @GetMapping("/admin/allskill")
    public String getAllSkill(Model model){
        List<Skill> skillList = skillService.getAllSkill();
        model.addAttribute("skillList", skillList);
        return "skills/all-skills";
    }

    @GetMapping("/admin/createskill")
    public String createSkill(){
        return "admin/skillCreate";
    }


    @PostMapping("/admin/createskill")
    public @ResponseBody
    String createSkill(@RequestParam String skillName){
        Skill skill = new Skill();

        skill.setName(skillName);
        skillService.save(skill);
        return "Saved successfully!";
    }


}
