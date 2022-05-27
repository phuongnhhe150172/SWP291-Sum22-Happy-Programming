package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.UserProfile;

@Repository
public interface IUserProfileRepository extends JpaRepository<UserProfile, Long> {

}