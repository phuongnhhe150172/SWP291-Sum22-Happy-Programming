package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.MentorExperience;
import swp.happyprogramming.model.MentorExperienceId;

import java.util.List;

@Repository
public interface IMentorExperienceRepository extends JpaRepository<MentorExperience, MentorExperienceId> {

//    @Modifying
//    @Query("delete from MentorExperience as m where m.experienceId = 1")
//    void deleteAllWithExperienceIds(List<Long> experienceId);

}
