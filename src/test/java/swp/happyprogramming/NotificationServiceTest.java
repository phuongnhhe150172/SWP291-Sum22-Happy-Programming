package swp.happyprogramming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.context.SpringBootTest;
import swp.happyprogramming.dto.NotificationDTO;

import swp.happyprogramming.model.Notification;

import swp.happyprogramming.repository.INotificationRepository;
import swp.happyprogramming.services.IUserService;
import swp.happyprogramming.utility.Utility;


import java.util.Date;
import java.util.Optional;


@SpringBootTest
public class NotificationServiceTest {
    @Autowired
    private IUserService userService;
    @Autowired
    private INotificationRepository notificationRepository;

    @Test
    public void mapNotification(){
        Optional<Notification> notification = notificationRepository.findById(new Long(9));
        Date modifiedDate = notification.get().getModified();
        System.out.println(notification.get().getCreated());

        NotificationDTO notificationDTO = Utility.mapNotification(notification.get());
        System.out.println(notificationDTO.getId());
        System.out.println("NotificationDTO time: "+notificationDTO.getTime());
    }


}
