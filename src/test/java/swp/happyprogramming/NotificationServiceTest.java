package swp.happyprogramming;

import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import swp.happyprogramming.adapter.dto.NotificationDTO;
import swp.happyprogramming.adapter.port.out.INotificationRepository;
import swp.happyprogramming.application.port.usecase.IUserService;
import swp.happyprogramming.domain.model.Notification;
import swp.happyprogramming.utility.Utility;


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
