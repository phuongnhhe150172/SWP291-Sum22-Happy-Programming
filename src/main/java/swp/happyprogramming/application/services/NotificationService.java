package swp.happyprogramming.application.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import swp.happyprogramming.adapter.port.out.INotificationRepository;
import swp.happyprogramming.application.usecase.INotificationService;
import swp.happyprogramming.adapter.dto.NotificationDTO;
import swp.happyprogramming.domain.model.Notification;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Role;
import swp.happyprogramming.utility.Utility;

@Service
public class NotificationService implements INotificationService {

  @Autowired
  private INotificationRepository notificationRepository;
  ModelMapper mapper = new ModelMapper();

  @Override
  public List<NotificationDTO> getNotificationByRoles(Set<Role> roles) {
    List<Notification> notifications = notificationRepository.getNotificationsByNotificationToRolesIn(
      roles);
    return mapNotificationToDTO(notifications);
  }

  @Override
  public Pagination<NotificationDTO> getNotificationByRoles(int pageNumber, Set<Role> roles) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5,
      Sort.by(Sort.Direction.DESC, "modified"));
    Page<Notification> page = notificationRepository.getNotificationsByNotificationToRolesIn(
      pageRequest, roles);
    int totalPages = page.getTotalPages();
    List<Notification> notifications = page.getContent();
    List<NotificationDTO> notificationDTOS = mapNotificationToDTO(notifications);
    return Utility.getPagination(totalPages, notificationDTOS);
  }

  @Override
  public NotificationDTO getNotificationByID(long id) {
    Notification noti = notificationRepository.findById(id);
    if (noti == null) {
      return null;
    }
    NotificationDTO notiDTO = mapper.map(noti, NotificationDTO.class);
    return notiDTO;
  }

  @Override
  public List<NotificationDTO> getAllNotifications() {
    List<Notification> notifications = notificationRepository.findAll();
    return mapNotificationToDTO(notifications);
  }

  private static List<NotificationDTO> mapNotificationToDTO(List<Notification> notifications) {
    return notifications
      .stream()
      .map(Utility::mapNotification)
      .collect(Collectors.toList());
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