package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import swp.happyprogramming.model.Notification;
import swp.happyprogramming.model.Role;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import swp.happyprogramming.model.*;

import java.util.List;
import java.util.Set;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> getNotificationsByNotificationToRolesIn(Set<Role> roles);

    Page<Notification> getNotificationsByNotificationToRolesIn(Pageable pageable, Set<Role> roles);
    @Modifying
    @Transactional
    @Query(value = "Insert into role_has_notification(noti_id, role_id) values (?1,?2)", nativeQuery = true)
    void insertRoleHasNotification(long role_id, long noti_id);

    Notification findById(long id);

    @Query(value = "select role_id from role_has_notification where noti_id = ?1 ", nativeQuery = true)
    List<Integer> getNotiInform(long id);

    @Modifying
    @Transactional
    @Query(value = "delete from role_has_notification where noti_id = ?1 ", nativeQuery = true)
    void deleteNotiInform(long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE notification set content = ?1  where id = ?2 ", nativeQuery = true)
    void editContentNoti(String content, long id);
}
