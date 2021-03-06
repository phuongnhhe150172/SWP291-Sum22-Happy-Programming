package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.RequestDTO;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Request;
import swp.happyprogramming.repository.ISkillRepository;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.services.ISkillService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SkillService implements ISkillService {
    @Autowired
    private ISkillRepository skillRepository;

    @Override
    public Pagination<Skill> getAllSkill(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        Page<Skill> page = skillRepository.findAll(pageRequest);
        int totalPages = page.getTotalPages();
        List<Skill> skillList = page.getContent();
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        return new Pagination<>(skillList, pageNumbers);
    }

    @Override
    public List<Skill> getAllSkill() {
        return skillRepository.findAll();
    }

    @Override
    public Skill save(Skill skill) {
        skillRepository.save(skill);
        return skill;
    }

    @Override
    public ArrayList<Skill> getAllSkillByUserId(long userId) {
        return skillRepository.findAllByMentorId(userId);
    }

    @Override
    public Skill findSkillById(long id) {
        return skillRepository.getById(id);
    }
}
