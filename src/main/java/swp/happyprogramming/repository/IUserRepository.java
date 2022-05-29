package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.User;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO USER_ROLES (USER_ID, ROLE_ID) VALUES (?1, ?2)", nativeQuery = true)
    void addRoleUser(long userID, long roleID);

    @Query(value = "SELECT COUNT(*) FROM USER_ROLES WHERE USER_ID = ?1 AND ROLE_ID=1", nativeQuery = true)
    int checkMentor(long userID);

    @Query(value = "select count(*) from user_roles where role_id in (select id from roles where `name` = ?1)", nativeQuery = true)
    int countUsersByRolesLike(String role);

    @Query(value = "select * from user_roles where role_id in (select id from roles where `name` = ?1)", nativeQuery = true)
    List<User> findUsersByRole(String role);
}
