package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Notification;
import swp.happyprogramming.model.Role;

import java.util.List;
import java.util.Set;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> getNotificationsByNotificationToRolesIn(Set<Role> roles);
}
