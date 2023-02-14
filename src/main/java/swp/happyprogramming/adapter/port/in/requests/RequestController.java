package swp.happyprogramming.adapter.port.in.requests;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.adapter.dto.UserDTO;
import swp.happyprogramming.application.port.usecase.IRequestService;
import swp.happyprogramming.application.port.usecase.IUserService;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Request;
import swp.happyprogramming.domain.model.User;

@Controller
public class RequestController {

  @Autowired
  private IUserService userService;

  @Autowired
  private IRequestService requestService;

  @Autowired
  private HttpSession session;

  @Secured("ROLE_MENTOR")
  @GetMapping("/requests")
  public String requests(Model model,
    @RequestParam(required = false, defaultValue = "1") int pageNumber) {
    //    Nguyễn Huy Hoàng - view all received requests
    //    Current user's requests
    Object sessionObj = session.getAttribute("userInformation");
    UserDTO user = (UserDTO) sessionObj;

    Pagination<Request> requests = requestService.getRequestReceived(
      user.getId(), pageNumber);
    model.addAttribute("requests", requests.getPaginatedList());
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", requests.getPageNumbers().size());
    return "requests";
  }

  @Secured({"ROLE_MENTEE", "ROLE_MENTOR"})
  @GetMapping("/request/sent")
  public String getRequestSent(Model model,
    @RequestParam(required = false, defaultValue = "1") int pageNumber) {
    //  Trinh Trung Kien - 21 - view all sent requests (mentee)
    Authentication authentication = SecurityContextHolder.getContext()
      .getAuthentication();
    String email = authentication.getName();
    User user = userService.findByEmail(email);

    Pagination<Request> requests = requestService.getRequestSent(user.getId(),
      pageNumber);
    model.addAttribute("requests", requests.getPaginatedList());
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", requests.getPageNumbers().size());
    return "requests/request_sent";
  }

  @GetMapping("/request/sent/delete")
  public String deleteRequest(
    @RequestParam(required = true, value = "id") long requestId) {
    Authentication authentication = SecurityContextHolder.getContext()
      .getAuthentication();
    String email = authentication.getName();
    User user = userService.findByEmail(email);

    // verify if the request to be deleted belongs to that user
    List<Request> requestList = requestService.getRequestSent(user.getId());
    Request request = requestService.getRequestById(requestId);
    if (requestList.contains(request)) {
      requestService.deleteRequest(requestId);
    }

    return "redirect:/request/sent";
  }

  @GetMapping("/sent")
  public String sentRequest(@RequestParam(value = "from") String fromId,
    @RequestParam(value = "to") String toId) {
    try {
      long mentorId = Integer.parseInt(toId);
      long menteeId = Integer.parseInt(fromId);
      requestService.insertRequeset(mentorId, menteeId);
      return "redirect:cv?id=" + toId;
    } catch (NumberFormatException e) {
      return "redirect:index";
    }
  }

  @Secured("ROLE_MENTOR")
  @GetMapping("/request/accept")
  public String acceptRequest(@RequestParam(value = "id") long requestId) {
    Object sessionObj = session.getAttribute("userInformation");
    UserDTO user = (UserDTO) sessionObj;

    List<Request> requestList = requestService.getRequestReceived(user.getId());
    Request request = requestService.getRequestById(requestId);
    if (requestList.contains(request)) {
      requestService.acceptReceivedRequest(requestId);
    }

    return "redirect:/requests";
  }

  @Secured("ROLE_MENTOR")
  @GetMapping("/request/delete")
  public String deleteReceivedRequest(
    @RequestParam(value = "id") long requestId) {
    Object sessionObj = session.getAttribute("userInformation");
    UserDTO user = (UserDTO) sessionObj;

    List<Request> requestList = requestService.getRequestReceived(user.getId());
    if (requestList.stream().map(Request::getId)
      .anyMatch(id -> id == requestId)) {
      requestService.deleteRequest(requestId);
    }

    return "redirect:/requests";
  }
}
