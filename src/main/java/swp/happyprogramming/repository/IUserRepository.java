package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query(value = "select count(*) from user_roles where role_id in (select id from roles where `name` = ?1)", nativeQuery = true)
    int countUsersByRolesLike(String role);

    @Query(value = "select * from users where id in (select user_id from user_roles where role_id in (select id from roles where `name` = ?1))", nativeQuery = true)
    List<User> findUsersByRole(String role);

    @Query(value = "select r.status from request as r where r.mentor_id = ?1 and r.mentee_id = ?2", nativeQuery = true)
    Optional<Integer> statusRequestByMentorIdAndMenteeId(long mentorId, long menteeId);

    @Query(value = "select * from users where id in (select user1 from connections where user2 = (select id from users where email=?1) union select user2 from connections where user1 = (select id from users where email=?1))",
            nativeQuery = true)
    ArrayList<User> findConnectionsByEmail(String email);

    @Query(value = "select * from users where id in (select mentor_id from request where mentee_id = (select id from users where email=?1) union select mentee_id from request where mentor_id = (select id from users where email=?1))",
            nativeQuery = true)
    ArrayList<User> findRequestsByEmail(String email);

    User findByResetPasswordToken(String token);

    @Query(value = "SELECT count(*) FROM users where Year(created) = ?1 and (month(created) >= ?2 or month(created) <= ?3) group by(month(created));", nativeQuery = true)  
    List<Integer> getListAmountNewMentees(int currentYear, int originMonth, int currentMonth);
}
