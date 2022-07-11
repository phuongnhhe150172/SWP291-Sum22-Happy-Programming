package swp.happyprogramming.services;

import swp.happyprogramming.dto.NotificationDTO;
import swp.happyprogramming.model.Notification;
import swp.happyprogramming.model.Role;

import java.util.List;
import java.util.Set;

public interface INotificationService {
    List<NotificationDTO> getNotificationByRoles(Set<Role> roles);
    List<NotificationDTO> getAllNotifications();
    Notification save(Notification notification);
    void informNotiForRole(long noti_id, long role_id);
}
