package swp.happyprogramming.adapter.port.in.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.domain.dto.NotificationDTO;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Role;
import swp.happyprogramming.domain.model.User;
import swp.happyprogramming.application.usecase.INotificationService;
import swp.happyprogramming.application.usecase.IUserService;


import jakarta.servlet.http.HttpSession;
import java.util.HashSet;
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
