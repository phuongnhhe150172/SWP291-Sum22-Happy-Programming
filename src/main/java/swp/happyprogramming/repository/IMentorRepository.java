package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Mentor;
import swp.happyprogramming.model.Skill;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface IMentorRepository extends JpaRepository<Mentor,Long> {
    Optional<Mentor> findByUserID(long userID);

    @Modifying
    @Transactional
    @Query(value = "delete from mentor_experience where mentor_id = ?1 and experience_id = ?2",nativeQuery = true)
    void deleteByMentorIdAndExperienceId(long mentorId,long experienceId);

    @Modifying
    @Transactional
    @Query(value = "Insert into mentor_experience(mentor_id,experience_id) values (?1,?2)",nativeQuery = true)
    void insertByMentorIdAndExperienceId(long mentorId,long experienceId);
}