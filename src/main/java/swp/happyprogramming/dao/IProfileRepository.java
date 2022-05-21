package swp.happyprogramming.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.UserProfile;

import java.util.Optional;

@Repository
public interface IProfileRepository extends JpaRepository<UserProfile, Long> {
    public Optional<UserProfile> findByUserID(Long id);
}
