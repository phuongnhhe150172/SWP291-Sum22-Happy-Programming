package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.NotificationDTO;
import swp.happyprogramming.model.Notification;
import swp.happyprogramming.model.Role;
import swp.happyprogramming.repository.INotificationRepository;
import swp.happyprogramming.services.INotificationService;
import swp.happyprogramming.utility.Utility;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    private INotificationRepository notificationRepository;

    @Override
    public List<NotificationDTO> getNotificationByRoles(Set<Role> roles) {
        List<Notification> notifications = notificationRepository.getNotificationsByNotificationToRolesIn(roles);
        List<NotificationDTO> notificationDTOS = notifications
                .stream()
                .map(notification -> Utility.mapNotification(notification))
                .collect(Collectors.toList());
        return notificationDTOS;
    }

    @Override
    public List<NotificationDTO> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationDTO> notificationDTOS = notifications
        .stream()
        .map(notification -> Utility.mapNotification(notification))
        .collect(Collectors.toList());
            return notificationDTOS;
    }



    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void informNotiForRole(long noti_id, long role_id) {
        notificationRepository.insertRoleHasNotification(noti_id, role_id);
    }
}
