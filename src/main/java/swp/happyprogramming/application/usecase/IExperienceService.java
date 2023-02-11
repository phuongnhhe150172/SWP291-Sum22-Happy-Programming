package swp.happyprogramming.application.usecase;

import java.util.List;
import swp.happyprogramming.domain.model.Experience;

import java.util.ArrayList;

public interface IExperienceService {
    Experience save(Experience experience);

    ArrayList<Experience> getAllExperienceByProfileID(long id);

  //    delete experience by mentor id
  void deleteExperienceByMentorId(long id);

  //  save all
  List<Experience> saveAll(List<Experience> experiences);
}
