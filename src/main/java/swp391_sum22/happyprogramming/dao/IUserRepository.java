package swp391_sum22.happyprogramming.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import swp391_sum22.happyprogramming.model.User;

public interface IUserRepository extends JpaRepository<User, Long> {

}
