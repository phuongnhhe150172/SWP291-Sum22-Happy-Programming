package swp.happyprogramming.controllers.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.dto.NotificationDTO;
import swp.happyprogramming.model.Notification;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Role;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.INotificationService;
import swp.happyprogramming.services.IUserService;
import swp.happyprogramming.services.servicesimpl.NotificationService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    public String getNotifications(Model model, @RequestParam(required = false, defaultValue = "1") int pageNumber){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);


        Set<Role> roles = new HashSet<Role>(user.getRoles());

        Pagination<NotificationDTO> notification = notificationService.getNotificationByRoles(pageNumber, roles);
        model.addAttribute("notifications", notification.getPaginatedList());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", notification.getPageNumbers().size());

        return "notification/notifications";
    }

    

  

}
