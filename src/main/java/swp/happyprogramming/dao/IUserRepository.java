package swp.happyprogramming.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

}