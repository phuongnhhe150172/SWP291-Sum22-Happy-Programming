package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Mentor;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface IMentorRepository extends JpaRepository<Mentor, Long> {
    @Query(value = "select * from((select id, user_id, created, modified from mentor, \n" +
            "(select received_id, avg(rate) as avg_rate from feedback group by received_id) \n" +
            "as feedback_count \n" +
            "where mentor.user_id = feedback_count.received_id\n" +
            "order by avg_rate desc)\n" +
            "union distinct\n" +
            "(select * from mentor where user_id not in (select received_id from feedback)\n" +
            "order by created desc)) as top_mentor limit 10", nativeQuery = true)
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

    @Query(value = "select * from mentor order by id desc limit 1", nativeQuery = true)
    Mentor findMentorLast();

    @Query(value = "select mentor.* from mentor join users on mentor.user_id = users.id where  users.firstname  like  CONCAT('%', ?1, '%') or users.lastname like  CONCAT('%', ?1, '%')", nativeQuery = true)
    List<Mentor> filterMentor(String word);
}
