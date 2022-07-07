package swp.happyprogramming.controllers.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String requests(Model model, @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        //    Nguyễn Huy Hoàng - view all received requests
        //    Current user's requests
        Object sessionObj = session.getAttribute("userInformation");
        if (sessionObj == null) return "redirect:/login";
        UserDTO user = (UserDTO) sessionObj;

        Pagination<Request> requests = requestService.getRequestReceived(user.getId(), pageNumber);
        model.addAttribute("requests", requests.getPaginatedList());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", requests.getPageNumbers().size());
        return "requests";
    }

    @GetMapping("/request/sent")
    public String getRequestSent(Model model, @RequestParam(required = false, defaultValue = "1") int pageNumber) {
        //  Trinh Trung Kien - 21 - view all sent requests (mentee)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        Pagination<Request> requests = requestService.getRequestSent(user.getId(), pageNumber);
        model.addAttribute("requests", requests.getPaginatedList());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", requests.getPageNumbers().size());
        return "requests/request_sent";
    }

    @GetMapping("/request/sent/delete")
    public String deleteRequest(@RequestParam(required = true, value = "id") long requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        // verify if the request to be deleted belongs to that user
        List<Request> requestList = requestService.getRequestSent(user.getId());
        Request request = requestService.getRequestById(requestId);
        if (requestList.contains(request)) requestService.deleteRequest(requestId);

        return "redirect:/request/sent";
    }

    @GetMapping("/sent")
    public String sentRequest(@RequestParam(value = "from") String fromId, @RequestParam(value = "to") String toId) {
        try {
            long mentorId = Integer.parseInt(toId);
            long menteeId = Integer.parseInt(fromId);
            requestService.insertRequeset(mentorId, menteeId);
            return "redirect:cv?id=" + toId;
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @GetMapping("/request/accept")
    public String acceptRequest(@RequestParam(value = "id") long requestId) {
        Object sessionObj = session.getAttribute("userInformation");
        if (sessionObj == null) return "redirect:/login";
        UserDTO user = (UserDTO) sessionObj;

        List<Request> requestList = requestService.getRequestReceived(user.getId());
        Request request = requestService.getRequestById(requestId);
        if (requestList.contains(request)) requestService.acceptReceivedRequest(requestId);

        return "redirect:/requests";
    }

    @GetMapping("/request/delete")
    public String deleteReceivedRequest(@RequestParam(value = "id") long requestId) {
        Object sessionObj = session.getAttribute("userInformation");
        if (sessionObj == null) return "redirect:/login";
        UserDTO user = (UserDTO) sessionObj;

        List<Request> requestList = requestService.getRequestReceived(user.getId());
        Request request = requestService.getRequestById(requestId);
        if (requestList.contains(request)) requestService.deleteReceivedRequest(requestId);

        return "redirect:/requests";
    }
}
