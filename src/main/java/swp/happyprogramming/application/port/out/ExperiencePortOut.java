package swp.happyprogramming.application.port.out;

import java.util.ArrayList;
import java.util.List;
import swp.happyprogramming.domain.model.Experience;

public interface ExperiencePortOut {

  ArrayList<Experience> findByMentorId(long id);

  ArrayList<Experience> findExperienceLast(int number);

  void deleteAllByIdIn(List<Long> listIdExperience);

  Experience save(Experience experience);
}
