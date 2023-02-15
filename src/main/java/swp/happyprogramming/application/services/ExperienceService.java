package swp.happyprogramming.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.application.port.out.ExperiencePortOut;
import swp.happyprogramming.application.port.usecase.IExperienceService;
import swp.happyprogramming.domain.model.Experience;

@Service
public class ExperienceService implements IExperienceService {

  @Autowired
  private ExperiencePortOut experienceRepository;

  @Override
  public ArrayList<Experience> getAllExperienceByProfileID(long id) {
    return experienceRepository.findByMentorId(id);
  }

  @Override
  public Experience save(Experience experience) {
    experienceRepository.save(experience);
    return experience;
  }

  //    delete experience by mentor id
  @Override
  public void deleteExperienceByMentorId(long id) {
    ArrayList<Experience> listExperience = experienceRepository.findByMentorId(
      id);
    List<Long> listIdExperience = listExperience.stream().map(Experience::getId)
      .collect(Collectors.toList());
    experienceRepository.deleteAllById(listIdExperience);
  }

  //  save all
  @Override
  public List<Experience> saveAll(List<Experience> experiences) {
    return experienceRepository.saveAll(experiences);
  }
}
