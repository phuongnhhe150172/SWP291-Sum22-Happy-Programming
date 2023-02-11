package swp.happyprogramming.adapter.port.in;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.application.usecase.IConnectService;
import swp.happyprogramming.application.usecase.IFeedbackService;
import swp.happyprogramming.application.usecase.INotificationService;
import swp.happyprogramming.application.usecase.IPostService;
import swp.happyprogramming.application.usecase.IRequestService;
import swp.happyprogramming.application.usecase.ISkillService;
import swp.happyprogramming.application.usecase.IUserService;
import swp.happyprogramming.adapter.dto.ConnectDTO;
import swp.happyprogramming.adapter.dto.MentorDTO;
import swp.happyprogramming.adapter.dto.NotificationDTO;
import swp.happyprogramming.adapter.dto.PostDTO;
import swp.happyprogramming.adapter.dto.UserDTO;
import swp.happyprogramming.domain.model.Feedback;
import swp.happyprogramming.domain.model.Method;
import swp.happyprogramming.domain.model.Notification;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Post;
import swp.happyprogramming.domain.model.Request;
import swp.happyprogramming.domain.model.Skill;
import swp.happyprogramming.domain.vo.FeedbackVo;
import swp.happyprogramming.domain.vo.PostVo;
import swp.happyprogramming.utility.Utility;

@Controller
@RequestMapping("/admin")
public class AdminController {

  @Autowired
  private IUserService userService;

  @Autowired
  private ISkillService skillService;

  @Autowired
  private IRequestService requestService;

  @Autowired
  private IPostService postService;

  @Autowired
  private IConnectService connectService;

  @Autowired
  private INotificationService notificationService;

  @Autowired
  private IFeedbackService feedbackService;

  @GetMapping("/dashboard")
  public String displayDashboardAdmin(Model model) {
    int totalNumberOfMentors = userService.countUsersByRolesLike("ROLE_MENTOR");
    int totalNumberOfMentees = userService.countUsersByRolesLike("ROLE_MENTEE");
    long totalNumberOfRequests = requestService.countTotalRequest();
    List<Integer> numberNewMentees = userService.getMonthlyNewMentees();

    model.addAttribute("totalNumberOfMentors", totalNumberOfMentors);
    model.addAttribute("totalNumberOfMentees", totalNumberOfMentees);
    model.addAttribute("totalNumberOfRequests", totalNumberOfRequests);
    model.addAttribute("numberNewMentees", numberNewMentees);
    return "admin/admin_dashboard";
  }

  @GetMapping("/mentees")
  public String showAllMentees(Model model, @RequestParam Map<String, String> params,
    @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber) {
    String rawFirstName = params.get("first_name");
    String rawLastName = params.get("last_name");
    String rawPhone = params.get("phone");
    String rawEmail = params.get("email");
    String firstName =
      (rawFirstName == null || rawFirstName.trim() == "") ? null : rawFirstName.trim();
    String lastName = (rawLastName == null || rawLastName.trim() == "") ? null : rawLastName.trim();
    String phone = (rawPhone == null || rawPhone.trim() == "") ? null : rawPhone.trim();
    String email = (rawEmail == null || rawEmail.trim() == "") ? null : rawEmail.trim();

    Pagination<UserDTO> page = userService.getMentees(pageNumber, firstName, lastName, phone,
      email);
    List<UserDTO> mentees = page.getPaginatedList();
    int totalNumberOfMentees = userService.countUsersByRolesLike("ROLE_MENTEE");
    int totalMenteesFound = mentees.size();
    model.addAttribute("mentees", page.getPaginatedList());
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", page.getPageNumbers().size());
    model.addAttribute("first_name", firstName);
    model.addAttribute("last_name", lastName);
    model.addAttribute("totalMentees", totalNumberOfMentees);
    model.addAttribute("totalMenteesFound", totalMenteesFound);
    model.addAttribute("phone", phone);
    model.addAttribute("email", email);
    return "admin/all-mentees";
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/mentors")
  public String showMentors(Model model,
    @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber) {
    // Nguyễn Huy Hoàng - 46 - List all mentors (admin)
    Pagination<MentorDTO> page = userService.getMentors(pageNumber);
    model.addAttribute("mentors", page.getPaginatedList());
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", page.getPageNumbers().size());
    return "admin/all-mentors";
  }

  @GetMapping("/mentor")
  public String viewMentor(Model model,
    @RequestParam(value = "id", required = false) long mentorId) {
    MentorDTO mentorDTO = userService.findMentor(mentorId);
    model.addAttribute("mentor", mentorDTO);
    return "admin/view-mentor";
  }

  @GetMapping("/mentee")
  public String showMentee(Model model,
    @RequestParam(value = "id", required = false) long menteeId) {
    UserDTO mentee = userService.findUser(menteeId);
    model.addAttribute("mentee", mentee);
    return "admin/view-mentee";
  }

  @GetMapping("/delete")
  public String deleteMentee(@RequestParam(value = "id", required = false) long menteeId) {
    userService.removeMentee(menteeId);
    return "redirect:/admin/mentees";
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/skills")
  public String getAllSkill(Model model,
    @RequestParam(required = false, defaultValue = "1") int pageNumber) {
    Pagination<Skill> skills = skillService.getAllSkill(pageNumber);
    model.addAttribute("skills", skills.getPaginatedList());
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", skills.getPageNumbers().size());
    return "admin/all-skills";
  }

  @GetMapping("/create-skill")
  public String createSkill() {
    return "admin/create-skill";
  }


  @PostMapping("/create-skill")
  public String createSkill(@RequestParam String skillName) {
    Skill skill = new Skill();
    skill.setName(skillName);
    skillService.save(skill);
    return "redirect:/admin/skills";
  }

  @GetMapping("create-notification")
  public String createNotifications(Model model) {
    // model.addAttribute("notifications", notifications);
    return "admin/admin-create-notification";
  }

  @PostMapping("create-notification")
  public String createNewNotifications(Model model, @RequestParam Map<String, Object> params) {
    // model.addAttribute("notifications", notifications);

    String content = String.valueOf(params.get("content"));
    Notification noti = new Notification();
    noti.setContent(content);
    Notification savedNoti = notificationService.save(noti);

    long notiId = savedNoti.getId();

    if (params.containsKey("mentor")) {
      notificationService.informNotiForRole(notiId, 1);
    }
    if (params.containsKey("mentee")) {
      notificationService.informNotiForRole(notiId, 2);
    }
    if (params.containsKey("admin")) {
      notificationService.informNotiForRole(notiId, 3);
    }

    return "redirect:/admin/notification";
  }

  @GetMapping("edit-notification")
  public String editNotifications(Model model,
    @RequestParam(value = "id", required = false) long id) {
    // model.addAttribute("notifications", notifications);

    NotificationDTO ndt = notificationService.getNotificationByID(id);

    List<Integer> inform = notificationService.getNotiInform(id);

    String content = ndt.getContent();
    Boolean role1 = false;
    Boolean role2 = false;
    Boolean role3 = false;

    for (Integer ii : inform) {
      if (ii == 1) {
        role1 = true;
      }
      if (ii == 2) {
        role2 = true;
      }
      if (ii == 3) {
        role3 = true;
      }
    }
    model.addAttribute("id", id);
    model.addAttribute("content", content);
    model.addAttribute("role1", role1);
    model.addAttribute("role2", role2);
    model.addAttribute("role3", role3);

    return "admin/admin_edit_notification";
  }


  @PostMapping("edit-notification")
  public String editNotifications(Model model, @RequestParam Map<String, Object> params) {
    // model.addAttribute("notifications", notifications);
    long notiId = Long.parseLong(String.valueOf(params.get("id")));

    String content = String.valueOf(params.get("content"));
    notificationService.removeInform(notiId);
    notificationService.editContentNoti(content, notiId);
    if (params.containsKey("mentor")) {
      notificationService.informNotiForRole(notiId, 1);
    }
    if (params.containsKey("mentee")) {
      notificationService.informNotiForRole(notiId, 2);
    }
    if (params.containsKey("admin")) {
      notificationService.informNotiForRole(notiId, 3);
    }

    return "redirect:/admin/notification";
  }

  @GetMapping("/update-skill")
  public String showSkillToUpdate(Model model,
    @RequestParam(value = "id", required = false) long id) {
    Skill skill = skillService.findSkillById(id);
    model.addAttribute("skill", skill);
    return "admin/update-skill";
  }

  @PostMapping("/update-skill")
  public String updateSkill(@RequestParam String skillId, @RequestParam String skillName) {
    Skill skill = new Skill();
    long id = Integer.parseInt(String.valueOf(skillId));
    skill.setId(id);
    skill.setName(skillName);
    skillService.save(skill);
    return "redirect:/admin/skills";
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/requests")
  public String showAllRequests(Model model,
    @RequestParam(required = false, defaultValue = "1") int pageNumber) {
    // Trinh Trung Kien - 52 - View all requests (admin)
    Pagination<Request> requests = requestService.getAllRequest(pageNumber);
    model.addAttribute("requests", requests.getPaginatedList());
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", requests.getPageNumbers().size());
    return "admin/all-requests";
  }

  @GetMapping("/posts")
  public String viewAllPost(Model model,
    @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber) {
    ArrayList<Post> posts = (ArrayList<Post>) postService.getAllPosts();
    ArrayList<PostVo> result = new ArrayList<>();

    for (Post p : posts) {
      PostDTO postDTO = postService.getPostById(p.getId());
      UserDTO userDTO = postDTO.getUser();
      Method methodDTO = postDTO.getMethod();
      int liked = userService.checkCared(userDTO.getId(), p.getId());
      PostVo pi = new PostVo(postDTO.getId(), userDTO.getImage(),
        userDTO.getFirstName() + userDTO.getLastName(), postDTO.getDescription(),
        postDTO.getStatus(), postDTO.getPrice(), methodDTO.getName());
      pi.setLiked(liked);
      result.add(pi);
    }

    model.addAttribute("posts", result);
    return "/admin/admin_post";
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/connections")
  public String viewAllConn(Model model,
    @RequestParam(required = false, defaultValue = "1") int pageNumber) {
    Pagination<ConnectDTO> connects = connectService.findAllConnections(pageNumber);
    model.addAttribute("connections", connects.getPaginatedList());
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", connects.getPageNumbers().size());
    return "admin/all-connections";
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/enable")
  public String enableUser(@RequestParam(value = "id", required = false) int id,
    @RequestParam(value = "status", required = false) int status,
    @RequestParam(value = "page", required = false) int page) {
    userService.enableUser(id);
    if (status == 1) {
      return "redirect:mentors?pageNumber=" + page;
    } else {
      return "redirect:mentees?pageNumber=" + page;
    }

  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/disable")
  public String disableUser(@RequestParam(value = "id", required = false) int id,
    @RequestParam(value = "status", required = false) int status,
    @RequestParam(value = "page", required = false) int page) {
    userService.disableUser(id);
    if (status == 1) {
      return "redirect:mentors?pageNumber=" + page;
    } else {
      return "redirect:mentees?pageNumber=" + page;
    }
  }

  @GetMapping("notification")
  public String getAllNotifications(Model model) {

    List<NotificationDTO> notifications = notificationService.getAllNotifications();
    model.addAttribute("notifications", notifications);

    return "admin/admin_notification";
  }

  @GetMapping("/feedback")
  public String showAllFeedback(Model model) {
    //    show user feedback

    ArrayList<FeedbackVo> feedbacks = new ArrayList<>();
    List<Feedback> feedbacksRaw = feedbackService.getAllFeedBack();
    for (Feedback f : feedbacksRaw) {
      UserDTO sender = Utility.mapUser(f.getSender());
      UserDTO receiver = Utility.mapUser(f.getReceiver());
      FeedbackVo fv = new FeedbackVo(sender.getFirstName() + " " + sender.getLastName(),
        f.getRate(), f.getComment());
      fv.setReceiverName(receiver.getFirstName() + " " + receiver.getLastName());
      feedbacks.add(fv);
    }
    feedbacks.sort((lhs, rhs) -> rhs.getRate().compareTo(lhs.getRate()));
    model.addAttribute("feedbacks", feedbacks);
    return "admin/feedback";
  }

}
