package swp.happyprogramming.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.UserProfile;
@Repository
public interface IProfileRepository extends JpaRepository<UserProfile, Long> {
}
