package swp.happyprogramming.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.services.servicesimpl.SkillService;

import java.util.List;

@Controller
public class SkillController {
    @Autowired
    private SkillService skillService;

    @GetMapping("/listskill")
    public @ResponseBody
    List<Skill> getAllSkill(){
//        Skill skill1 = new Skill();
//        skill1.setSkillName("Java");
//        Skill skill2 = new Skill();
//        skill2.setSkillName("Python");
//        skillService.save(skill1);
//        skillService.save(skill2);

        return skillService.getAllSkill();
    }

}
