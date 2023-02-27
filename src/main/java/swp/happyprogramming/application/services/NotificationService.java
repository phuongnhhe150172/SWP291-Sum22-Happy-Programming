package swp.happyprogramming.application.services;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import swp.happyprogramming.application.dto.NotificationDTO;
import swp.happyprogramming.application.port.out.NotificationPortOut;
import swp.happyprogramming.application.port.usecase.INotificationService;
import swp.happyprogramming.domain.model.Notification;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Role;
import swp.happyprogramming.utility.Utility;

@AllArgsConstructor
public class NotificationService implements INotificationService {

  private NotificationPortOut notificationRepository;

  @Override
  public List<NotificationDTO> getNotificationByRoles(Set<Role> roles) {
    List<Notification> notifications = notificationRepository.getNotificationsByNotificationToRolesIn(
      roles);
    return Utility.mapList(notifications, Utility::mapNotification);
  }

  @Override
  public Pagination<NotificationDTO> getNotificationByRoles(int pageNumber,
    Set<Role> roles) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5,
      Sort.by(Sort.Direction.DESC, "modified"));
    Page<Notification> page = notificationRepository.getNotificationsByNotificationToRolesIn(
      pageRequest, roles);
    return Utility.getPagination(page, Utility::mapNotification);
  }

  @Override
  public NotificationDTO getNotificationByID(long id) {
    Notification noti = notificationRepository.findById(id);
    return Utility.mapNotification(noti);
  }

  @Override
  public List<NotificationDTO> getAllNotifications() {
    List<Notification> notifications = notificationRepository.findAll();
    return Utility.mapList(notifications, Utility::mapNotification);
  }

  @Override
  public List<Integer> getNotiInform(long id) {
    return notificationRepository.getNotiInform(id);
  }

  @Override
  public Notification save(Notification notification) {
    return notificationRepository.save(notification);
  }

  @Override
  public void informNotiForRole(long noti_id, long role_id) {
    notificationRepository.insertRoleHasNotification(noti_id, role_id);
  }

  @Override
  public void removeInform(long id) {
    notificationRepository.deleteNotiInform(id);
  }

  @Override
  public void editContentNoti(String content, long id) {
    notificationRepository.editContentNoti(content, id);
  }
}