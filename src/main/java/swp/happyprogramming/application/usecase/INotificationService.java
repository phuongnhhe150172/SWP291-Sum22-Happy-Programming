package swp.happyprogramming.application.usecase;

import swp.happyprogramming.adapter.dto.NotificationDTO;
import swp.happyprogramming.domain.model.Notification;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Role;

import java.util.List;
import java.util.Set;

public interface INotificationService {
    List<NotificationDTO> getNotificationByRoles(Set<Role> roles);
    NotificationDTO getNotificationByID(long id);
    Pagination<NotificationDTO> getNotificationByRoles(int pageNumber, Set<Role> roles);
    List<NotificationDTO> getAllNotifications();
    Notification save(Notification notification);
    void informNotiForRole(long noti_id, long role_id);
    List<Integer> getNotiInform(long id);
    void removeInform(long id);
    void editContentNoti(String content, long id);
}
