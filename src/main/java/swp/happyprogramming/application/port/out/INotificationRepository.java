package swp.happyprogramming.application.port.out;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import swp.happyprogramming.domain.model.Notification;
import swp.happyprogramming.domain.model.Role;

public interface INotificationRepository {

  List<Notification> getNotificationsByNotificationToRolesIn(Set<Role> roles);

  Page<Notification> getNotificationsByNotificationToRolesIn(Pageable pageable,
    Set<Role> roles);

  void insertRoleHasNotification(long role_id, long noti_id);

  Notification findById(long id);

  List<Integer> getNotiInform(long id);

  void deleteNotiInform(long id);

  void editContentNoti(String content, long id);

  Notification save(Notification notification);

  List<Notification> findAll();
}