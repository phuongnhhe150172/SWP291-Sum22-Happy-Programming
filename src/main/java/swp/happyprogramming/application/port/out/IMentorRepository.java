package swp.happyprogramming.application.port.out;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import swp.happyprogramming.domain.model.Mentor;


public interface IMentorRepository {

  List<Mentor> getTopMentors();

  Optional<Mentor> findByUserId(long userID);


  void deleteByMentorIdAndExperienceId(long mentorId, long experienceId);


  void insertByMentorIdAndExperienceId(long mentorId, long experienceId);


  void addSkillUser(long userId, long skillId);


  void deleteByUserIdAndSkillId(long userId, long skillId);


  Mentor findMentorLast();


  List<Mentor> filterMentor(String word);

  Mentor save(Mentor mentor);

  Page<Mentor> findAll(PageRequest pageRequest);
}
