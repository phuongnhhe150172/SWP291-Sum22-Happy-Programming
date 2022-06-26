package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.model.Mentor;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface IMentorRepository extends JpaRepository<Mentor, Long> {
    @Query(value = "select * from mentor limit 10", nativeQuery = true)
    List<Mentor> getTopMentors();

    Optional<Mentor> findByUserId(long userID);

    @Modifying
    @Transactional
    @Query(value = "delete from mentor_experience where mentor_id = ?1 and experience_id = ?2", nativeQuery = true)
    void deleteByMentorIdAndExperienceId(long mentorId, long experienceId);

    @Modifying
    @Transactional
    @Query(value = "Insert into mentor_experience(mentor_id,experience_id) values (?1,?2)", nativeQuery = true)
    void insertByMentorIdAndExperienceId(long mentorId, long experienceId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_skills (mentor_id,skill_id) values (?1,?2)", nativeQuery = true)
    void addSkillUser(long userId, long skillId);

    @Modifying
    @Transactional
    @Query(value = "delete from user_skills where mentor_id = ?1 and skill_id = ?2", nativeQuery = true)
    void deleteByUserIdAndSkillId(long userId, long skillId);
}
