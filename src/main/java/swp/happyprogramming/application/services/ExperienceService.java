package swp.happyprogramming.application.services;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.adapter.port.out.IExperienceRepository;
import swp.happyprogramming.application.usecase.IExperienceService;
import swp.happyprogramming.domain.model.Experience;

@Service
public class ExperienceService implements IExperienceService {
    @Autowired
    private IExperienceRepository experienceRepository;

    @Override
    public ArrayList<Experience> getAllExperienceByProfileID(long id) {
        return experienceRepository.findByMentorId(id);
    }

    @Override
    public Experience save(Experience experience) {
        experienceRepository.save(experience);
        return experience;
    }
}
