package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dao.ISkillRepository;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.services.ISkillService;

import java.util.List;

@Service
public class SkillService implements ISkillService {
    @Autowired
    private ISkillRepository skillRepository;

    @Override
    public List<Skill> getAllSkill() {
        return skillRepository.findAll();
    }

    @Override
    public Skill save(Skill skill) {
        skillRepository.save(skill);
        return skill;
    }

}
