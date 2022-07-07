package swp.happyprogramming.services;

import swp.happyprogramming.model.Notification;
import swp.happyprogramming.model.Role;

import java.util.List;
import java.util.Set;

public interface INotificationService {
    List<Notification> getNotificationByRoles(Set<Role> roles);
}
