package swp.happyprogramming.adapter.port.in.user;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.adapter.dto.DistrictDTO;
import swp.happyprogramming.adapter.dto.MentorDTO;
import swp.happyprogramming.adapter.dto.ProvinceDTO;
import swp.happyprogramming.adapter.dto.UserAvatarDTO;
import swp.happyprogramming.adapter.dto.UserDTO;
import swp.happyprogramming.adapter.dto.WardDTO;
import swp.happyprogramming.application.usecase.IConnectService;
import swp.happyprogramming.application.usecase.IDistrictService;
import swp.happyprogramming.application.usecase.IExperienceService;
import swp.happyprogramming.application.usecase.IMentorService;
import swp.happyprogramming.application.usecase.IProvinceService;
import swp.happyprogramming.application.usecase.IRequestService;
import swp.happyprogramming.application.usecase.ISkillService;
import swp.happyprogramming.application.usecase.IUserService;
import swp.happyprogramming.application.usecase.IWardService;
import swp.happyprogramming.domain.model.Experience;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Skill;
import swp.happyprogramming.domain.model.User;
import swp.happyprogramming.utility.Utility;

@Controller
public class UserController {

  private static final String INDEX_PAGE = "redirect:index";
  private static final String USER_SESSION = "userInformation";

  @Autowired
  private HttpSession session;

  @Autowired
  private IUserService userService;

  @Autowired
  private IProvinceService provinceService;

  @Autowired
  private IDistrictService districtService;

  @Autowired
  private IWardService wardService;

  @Autowired
  private IMentorService mentorService;

  @Autowired
  private IExperienceService experienceService;

  @Autowired
  private ISkillService skillService;

  @Autowired
  private IRequestService requestService;

  @Autowired
  private IConnectService connectService;

  @Secured("ROLE_MENTEE")
  @GetMapping("/profile")
  public String showUserProfile(Model model,
    @RequestParam(value = "id", required = false) String id) {
    //        Nguyễn Huy Hoàng - 04 - View public mentor profile
    //        Hoàng Văn Nam -   - View mentee profile
    Object sessionUser = session.getAttribute(USER_SESSION);
    if (sessionUser == null) {
      return "redirect:/login";
    }

    UserDTO user;
    if (id != null) {
      long userId = Integer.parseInt(id);
      user = userService.findUser(userId);
      UserDTO userDTO = (UserDTO) sessionUser;

      addStatusAttribute(model, user, userDTO);
    } else {
      user = (UserDTO) sessionUser;
    }
    String role = (String) session.getAttribute("role");
    model.addAttribute("user", user);
    model.addAttribute("role", role);
    return "user/user-profile";
  }

  @Secured("ROLE_MENTEE")
  @GetMapping("/update")
  public String updateUserProfile(Model model,
    @RequestParam(value = "id", required = false) String id) {
    //      Hoàng Văn Nam - Update profile mentee
    UserDTO user = id != null ?
      userService.findUser(Integer.parseInt(id)) :
      (UserDTO) session.getAttribute(USER_SESSION);
    addAddressAttribute(model, user);
    model.addAttribute("user", user);
    return "user/update-profile";
  }

  @Secured("ROLE_MENTEE")
  @PostMapping("/update")
  public String updateUserProfile(@ModelAttribute("user") UserDTO userDTO,
    @RequestParam Map<String, Object> params) {
    try {
      long wardId = Integer.parseInt(String.valueOf(params.get("wardId")));
      UserDTO user = userService.updateUserProfile(userDTO, wardId);
      // Update session
      session.setAttribute(USER_SESSION, user);
      return "redirect:profile";
    } catch (NumberFormatException e) {
      return INDEX_PAGE;
    }

  }

  @Secured({"ROLE_MENTOR", "ROLE_MENTEE"})
  @GetMapping("/cv")
  public String viewMentorCV(Model model, @RequestParam(value = "id", required = false) String id) {
    try {
      Object sessionUser = session.getAttribute(USER_SESSION);
      if (sessionUser == null) {
        return "redirect:/login";
      }
      UserDTO user = (UserDTO) sessionUser;
      MentorDTO mentor;
      if (id != null) {
        long mentorId = Integer.parseInt(id);
        mentor = mentorService.findMentor(mentorId);
        addStatusAttribute(model, mentor, user);
      } else {
        mentor = mentorService.findMentor(user.getId());
      }
      model.addAttribute("mentor", mentor);
      String role = (String) session.getAttribute("role");
      model.addAttribute("role", role);
      return "user/mentorcv";
    } catch (NumberFormatException e) {
      return INDEX_PAGE;
    }
  }

  @Secured({"ROLE_MENTOR", "ROLE_MENTEE"})
  @GetMapping("/update/cv")
  public String updateMentorCv(Model model,
    @RequestParam(value = "id", required = false) String id) {
    //      Hoàng Văn Nam -   - Update profile mentor
    try {
      long mentorId = Integer.parseInt(id);
      MentorDTO mentorDTO = mentorService.findMentor(mentorId);

      addAddressAttribute(model, mentorDTO);
      addSkillAttribute(model, mentorDTO);
      addExperienceAttribute(model, mentorDTO);

      model.addAttribute("mentor", mentorDTO);
      model.addAttribute("mentorId", mentorId);
      return "user/updatecv";
    } catch (NumberFormatException e) {
      return INDEX_PAGE;
    }
  }

  @Secured({"ROLE_MENTOR", "ROLE_MENTEE"})
  @PostMapping("/update/cv")
  public String updateMentorCv(@ModelAttribute("mentor") MentorDTO mentor,
    @RequestParam Map<String, Object> params,
    @RequestParam(value = "experieceValue", required = false) List<String> experieceValue,
    @RequestParam(value = "skillValue", required = false) List<String> skillValue) {
    //      Hoàng Văn Nam -   - Update profile mentor
    try {
      long wardId = Integer.parseInt(String.valueOf(params.get("wardId")));

      UserDTO user = mentorService.updateMentor(mentor, wardId, experieceValue, skillValue);
      session.setAttribute(USER_SESSION, user);
      return "redirect:../cv";
    } catch (NumberFormatException e) {
      return INDEX_PAGE;
    }
  }

  @Secured("ROLE_MENTEE")
  @PostMapping("/uploading")
  public String updateImage(@RequestParam("image") MultipartFile image) {
    //      Hoàng Văn Nam -   - Upload avatar
    UserDTO userDTO = (UserDTO) session.getAttribute(USER_SESSION);
    userService.updateImage(userDTO.getId(), image);

    session.setAttribute(USER_SESSION, userDTO);
    return "redirect:profile";
  }

  @Secured({"ROLE_MENTEE"})
  @GetMapping("/create")
  public String createCv(Model model, @RequestParam(value = "id", required = false) String id) {
    UserDTO user;
    if (id != null) {
      if (!Utility.isInteger(id)) {
        return INDEX_PAGE;
      }
      long userId = Integer.parseInt(id);
      user = userService.findUser(userId);
    } else {
      user = (UserDTO) session.getAttribute(USER_SESSION);
    }
    List<Skill> listSkill = skillService.getAllSkill();

    model.addAttribute("listSkill", listSkill);
    model.addAttribute("user", user);
    return "user/createcv";
  }

  @Secured({"ROLE_MENTEE"})
  @PostMapping("/create")
  public String createCv(@RequestParam(value = "id", required = false) String id,
    @RequestParam(value = "experieceValue", required = false) List<String> experieceValue,
    @RequestParam(value = "skillValue", required = false) List<String> skillValue) {
    try {
      long userId = Integer.parseInt(id);
      mentorService.createCv(userId, experieceValue, skillValue);
      String sessionRole = "MENTOR_AND_MENTEE";
      session.setAttribute("role", sessionRole);
      return "redirect:cv";
    } catch (NumberFormatException e) {
      return INDEX_PAGE;
    }
  }

  public void addAddressAttribute(Model model, UserDTO user) {
    long districtId = user.getAddress().getDistrict().getId();
    long provinceId = user.getAddress().getProvince().getId();

    List<ProvinceDTO> listProvinces = provinceService.findAllProvinces();
    List<DistrictDTO> listDistrict = districtService.findAllDistrict(provinceId);
    List<WardDTO> listWard = wardService.findAllWard(districtId);

    model.addAttribute("listProvinces", listProvinces);
    model.addAttribute("listDistrict", listDistrict);
    model.addAttribute("listWard", listWard);
  }

  public void addSkillAttribute(Model model, MentorDTO mentor) {
    List<Skill> listSkill = skillService.getAllSkill();
    Map<Skill, Integer> mapSkill = mentorService.findMapSkill(listSkill, mentor.getSkills());
    model.addAttribute("mapSkill", mapSkill);
  }

  public void addExperienceAttribute(Model model, MentorDTO mentor) {
    List<Experience> listExperience = experienceService.getAllExperienceByProfileID(
      mentor.getProfileId());
    model.addAttribute("listExperience", listExperience);
  }

  public void addStatusAttribute(Model model, UserDTO target, UserDTO currentUser) {
    Integer statusRequest =
      (requestService.findStatusRequest(target.getId(), currentUser.getId()) != null) ? 1 : 0;
    Integer statusConnect =
      (connectService.findConnectByUser1AndUser2(target.getId(), currentUser.getId()) != null ||
        connectService.findConnectByUser1AndUser2(currentUser.getId(), target.getId()) != null) ? 1
        : 0;
    model.addAttribute("statusConnect", statusConnect);
    model.addAttribute("statusRequest", statusRequest);
  }

  @GetMapping("/mentor")
  public String showMentor(Model model,
    @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    if (email.equalsIgnoreCase("anonymousUser")) {
      return "redirect:/login";
    }

    User user = userService.findByEmail(email);
    Pagination<MentorDTO> page = mentorService.getMentors(pageNumber);
    List<Long> connectedMentorIds = connectService.getConnectedMentor(user.getId());
    List<Long> requestedMentorIds = requestService.getRequestedMentorId(user.getId());

    model.addAttribute("connections", connectedMentorIds);
    model.addAttribute("requests", requestedMentorIds);
    model.addAttribute("mentorList", page.getPaginatedList());
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", page.getPageNumbers().size());
    return "mentor/showMentor";
  }

  @PostMapping("/mentor")
  public String showMentor(Model model, @RequestParam Map<String, Object> params) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    if (email.equalsIgnoreCase("anonymousUser")) {
      return "redirect:/login";
    }

    User user = userService.findByEmail(email);
    List<MentorDTO> page = mentorService.filterMentors(String.valueOf(params.get("search")));
    List<Long> connectedMentorIds = connectService.getConnectedMentor(user.getId());
    List<Long> requestedMentorIds = requestService.getRequestedMentorId(user.getId());

    model.addAttribute("connections", connectedMentorIds);
    model.addAttribute("requests", requestedMentorIds);
    model.addAttribute("mentorList", page);
    model.addAttribute("search", String.valueOf(params.get("search")));
    return "mentor/filterShowMentor";
  }

  @GetMapping("/mentor/top")
  public String topMentor(Model model) {
    List<UserAvatarDTO> mentorList = mentorService.getTopMentors();
    model.addAttribute("mentorList", mentorList);
    return "mentor/suggestions";
  }

  @GetMapping("/home")
  public String getHomePage() {
    return "redirect:/profile";
  }
}
