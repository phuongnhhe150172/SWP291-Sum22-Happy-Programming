package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.User;

import javax.transaction.Transactional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO USER_ROLES (USER_ID, ROLE_ID) VALUES (?1, ?2)", nativeQuery = true)
    void addRoleUser(long userID, long roleID);

    @Query(value = "SELECT COUNT(*) FROM USER_ROLES WHERE USER_ID = ?1 AND ROLE_ID=1", nativeQuery = true)
    int checkMentor(long userID);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_skills (user_id,skill_id) values (?1,?2)",nativeQuery = true)
    void addSkillUser(long userId,long skillId);

    @Modifying
    @Transactional
    @Query(value = "delete from user_skills where user_id = ?1 and skill_id = ?2",nativeQuery = true)
    void deleteByUserIdAndSkillId(long userId,long skillId);
}
