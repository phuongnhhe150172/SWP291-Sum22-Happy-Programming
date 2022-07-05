package swp.happyprogramming.controllers.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Notification;
import swp.happyprogramming.model.Role;
import swp.happyprogramming.model.User;
import swp.happyprogramming.repository.INotificationRepository;
import swp.happyprogramming.repository.IRoleRepository;
import swp.happyprogramming.services.INotificationService;
import swp.happyprogramming.services.IUserService;
import swp.happyprogramming.services.servicesimpl.NotificationService;


import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/notification/")
public class NotificationController {
    @Autowired
    private HttpSession session;

    @Autowired
    private INotificationService notificationService;

    @Autowired
    private IUserService userService;

    @GetMapping("all")
    public String getNotifications(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

//        List<Role> roleList = (List<)user.getRoles();
        Set<Role> roles = new HashSet<>();
        Set<Role> roles1 = new HashSet<Role>(user.getRoles());


        List<Notification> notifications = notificationService.getNotificationByRoles(roles1);
        model.addAttribute("notifications", notifications);
        return "notification/notifications";
    }

}
