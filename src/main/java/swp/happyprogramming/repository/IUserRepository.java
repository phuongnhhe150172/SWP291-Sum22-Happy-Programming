package swp.happyprogramming.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Role;
import swp.happyprogramming.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query(value = "select count(*) from user_roles where role_id in (select id from roles where `name` = ?1)", nativeQuery = true)
    int countUsersByRolesLike(String role);

    @Query(value = "SELECT * FROM users As a join user_roles as b ON a.id = b.user_id and b.role_id = 2", nativeQuery = true)
    Page<User> findMentees(Specification<User> specification,Pageable pageable);

    @Query(value = "select r.status from request as r where r.mentor_id = ?1 and r.mentee_id = ?2", nativeQuery = true)
    Optional<Integer> statusRequestByMentorIdAndMenteeId(long mentorId, long menteeId);

    @Query(value = "select * from users where id in " +
            "(select user1 from connections where user2 = ?1 union select user2 from connections where user1 = ?1)",
            nativeQuery = true)
    Page<User> findConnectionsById(Pageable pageable, long id);

    @Query(value = "select * from users where id in (select user1 from connections where user2 = ?1 union select user2 from connections where user1 = ?1)",
            nativeQuery = true)
    List<User> findConnectionsById(long id);

    @Query(value = "select * from users where id in (select mentor_id from request where mentee_id = (select id from users where email=?1) union select mentee_id from request where mentor_id = (select id from users where email=?1))",
            nativeQuery = true)
    ArrayList<User> findRequestsByEmail(String email);

    User findByResetPasswordToken(String token);

    @Modifying
    @Transactional
    @Query(value = "update users set status = 1 where id = ?1", nativeQuery = true)
    void enableUser(long id);

    @Modifying
    @Transactional
    @Query(value = "update users set status = 0 where id = ?1", nativeQuery = true)
    void disableUser(long id);

//    @Modifying
//    @Transactional
//    @Query(value = "update user_roles set role_id = 1 where user_id = ?1", nativeQuery = true)
//    void convertToMentor(long id);

    @Modifying
    @Transactional
    @Query(value = "insert into user_roles(user_id,role_id) values (?1,?2)", nativeQuery = true)
    void convertToMentor(long userId, long roleId);

    @Query(value = "select sum(case when b.created is null then 0 else 1 end) from\n" +
            "\t(\n" +
            "SELECT @N\\:=@N+1 AS 'month'" +
            "    FROM mysql.help_relation,(SELECT @N\\:=0) dum LIMIT 12\n" +
            "    ) as a \n" +
            "left join \n" +
            "(" +
            "\t\tSelect id, created \n" +
            "\t\tfrom users as a\n" +
            "\t\t\tjoin \n" +
            "\t\tuser_roles as b on a.id = b.user_id and b.role_id = 2\n" +
            "\t) as b on a.month =  month(b.created)\n" +
            "    group by(a.month);",nativeQuery = true)
    List<Integer> getListAmountNewMentees();
}
