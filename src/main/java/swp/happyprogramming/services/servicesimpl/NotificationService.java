package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.model.Notification;
import swp.happyprogramming.model.Role;
import swp.happyprogramming.repository.INotificationRepository;
import swp.happyprogramming.services.INotificationService;

import java.util.List;
import java.util.Set;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    private INotificationRepository notificationRepository;

    @Override
    public List<Notification> getNotificationByRoles(Set<Role> roles) {
        List<Notification> notifications = notificationRepository.getNotificationsByNotificationToRolesIn(roles);
        return notifications;
    }
}
