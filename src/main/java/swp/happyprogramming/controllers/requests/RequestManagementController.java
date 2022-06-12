package swp.happyprogramming.controllers.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import swp.happyprogramming.dto.ConnectionDTO;
import swp.happyprogramming.dto.RequestDTO;
import swp.happyprogramming.model.Request;
import swp.happyprogramming.services.IRequestService;
import swp.happyprogramming.services.IUserService;

import java.util.List;

@Controller
public class RequestManagementController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IRequestService requestService;

    //    This class will be used both for admin and user
    @GetMapping("/requests")
    public String requests(Model model) {
        //    Current user's requests
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        if (email.equalsIgnoreCase("anonymousUser")) {
            return "redirect:/login";
        }

        List<ConnectionDTO> requests = userService.getRequestsByEmail(email);
        model.addAttribute("requests", requests);
        return "requests";
    }

    //Display all request sent (mentee)
    @GetMapping("/request/sent/{id}")
    public String getRequestSent(@PathVariable int id, Model model){
        List<RequestDTO> list = requestService.getRequestSent(id);
        model.addAttribute("requestList", list);
        return "requests/request_sent";
    }

    //Display all requests (admin)
    @GetMapping("/admin/requests")
    public String showAllRequests(Model model){
        List<RequestDTO> requests = requestService.getAllRequest();
        model.addAttribute("requests",requests);
        return "requests/all-requests";
    }

}
