package swp.happyprogramming.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.services.servicesimpl.SkillService;

import java.util.List;

@Controller
public class SkillsManagementController {
    @Autowired
    private SkillService skillService;

    @GetMapping("/admin/listskill")
    public String getAllSkill(Model model){
//        Skill skill1 = new Skill();
//        skill1.setSkillName("Java");
//        Skill skill2 = new Skill();
//        skill2.setSkillName("Python");
//        skillService.save(skill1);
//        skillService.save(skill2);
        List<Skill> skillList = skillService.getAllSkill();
        model.addAttribute("skillList", skillList);
        return "showskill";
    }
    @PostMapping("/admin/createskill")
    public @ResponseBody
    String createSkill(Model model, @RequestParam String skillname){
        Skill skill = new Skill();

        skill.setName(skillname);
        skillService.save(skill);
        return "Saved successfully!";
    }


}
