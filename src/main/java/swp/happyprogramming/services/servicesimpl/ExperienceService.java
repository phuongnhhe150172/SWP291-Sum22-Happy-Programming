package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.repository.IExperienceRepository;
import swp.happyprogramming.model.Experience;
import swp.happyprogramming.services.IExperienceService;

import java.util.ArrayList;

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
