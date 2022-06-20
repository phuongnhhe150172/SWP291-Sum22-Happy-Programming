package swp.happyprogramming.controllers.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import swp.happyprogramming.dto.ConnectionDTO;
import swp.happyprogramming.dto.RequestDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Request;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IRequestService;
import swp.happyprogramming.services.IUserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class RequestManagementController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IRequestService requestService;

    @Autowired
    private HttpSession session;

    //    This class will be used both for admin and user
    @GetMapping("/requests")
    public String requests(Model model) {
        //    Nguyễn Huy Hoàng - view all received requests
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

    @GetMapping("/request/sent")
    public String getRequestSent(Model model, @RequestParam(required = false,defaultValue = "1") int pageNumber){
        //  Trinh Trung Kien - 21 - view all sent requests (mentee)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        Pagination<RequestDTO> requests = requestService.getRequestSent(user.getId(), pageNumber);
        model.addAttribute("requests",requests.getPaginatedList());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", requests.getPageNumbers().size());
        return "requests/request_sent";
    }

    @GetMapping("/request/delete")
    public String deleteRequest(@RequestParam(required = true) long requestId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        //verify if the request to be deleted belongs to that user
        List<RequestDTO> requestDTOS = requestService.getRequestSent(user.getId());
        RequestDTO request = requestService.getRequestById(requestId);
        if (requestDTOS.contains(request)) requestService.deleteRequest(requestId);

        return "redirect:/request/sent";
    }
}
