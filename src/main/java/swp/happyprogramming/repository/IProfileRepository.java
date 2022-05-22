package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.UserProfile;

import java.util.Optional;

@Repository
public interface IProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserID(long userID);
}
