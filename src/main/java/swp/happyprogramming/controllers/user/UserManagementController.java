package swp.happyprogramming.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import swp.happyprogramming.dto.*;
import swp.happyprogramming.model.Experience;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.services.*;
import swp.happyprogramming.utility.Utility;

import javax.servlet.http.HttpSession;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Controller
public class UserManagementController {
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
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

    @GetMapping("/profile")
    public String showUserProfile(Model model,
                                  @RequestParam(value = "id", required = false) String id) {
        //        Nguyễn Huy Hoàng - 04 - View public mentor profile
        //        Hoàng Văn Nam -   - View mentee profile
        Object sessionUser = session.getAttribute(USER_SESSION);
        if (sessionUser == null) return "redirect:/login";

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

    @GetMapping("/update")
    public String updateUserProfile(Model model, @RequestParam(value = "id", required = false) String id) {
        //      Hoàng Văn Nam - Update profile mentee
        UserDTO user = id != null ?
                userService.findUser(Integer.parseInt(id)) :
                (UserDTO) session.getAttribute(USER_SESSION);
        addAddressAttribute(model, user);
        model.addAttribute("user", user);
        return "user/update-profile";
    }

    @PostMapping("/update")
    public String updateUserProfile(@ModelAttribute("user") UserDTO userDTO,
                                    @RequestParam Map<String, Object> params) {
        //      Hoàng Văn Nam -   - Update profile mentee
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

    @GetMapping("/cv")
    public String viewMentorCV(Model model, @RequestParam(value = "id", required = false) String id) {
        //      Hoàng Văn Nam -   - View profile mentor
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

    @GetMapping("/update/cv")
    public String updateMentorCv(Model model, @RequestParam(value = "id", required = false) String id) {
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

    @PostMapping("/update/cv")
    public String updateMentorCv(@ModelAttribute("mentor") MentorDTO mentor,
                                 @RequestParam Map<String, Object> params,
                                 @RequestParam(value = "experieceValue", required = false) List<String> experieceValue,
                                 @RequestParam(value = "skillValue", required = false) List<String> skillValue) {
        //      Hoàng Văn Nam -   - Update profile mentor
        try {
            long wardId = Integer.parseInt(String.valueOf(params.get("wardId")));

            UserDTO user =  mentorService.updateMentor(mentor, wardId, experieceValue, skillValue);
            session.setAttribute(USER_SESSION, user);
            return "redirect:../cv";
        } catch (NumberFormatException e) {
            return INDEX_PAGE;
        }
    }

    @PostMapping("/uploading")
    public String updateImage(@RequestParam("image") MultipartFile image) {
        //      Hoàng Văn Nam -   - Upload avatar
        UserDTO userDTO = (UserDTO) session.getAttribute(USER_SESSION);
        userService.updateImage(userDTO.getId(), CURRENT_FOLDER, image);
        userDTO.setImage("/upload/static/imgs/image" + userDTO.getId() + ".jpg");
        session.setAttribute(USER_SESSION, userDTO);
        return "redirect:profile";
    }

    @GetMapping("/create")
    public String createCv(Model model, @RequestParam(value = "id", required = false) String id) {
        UserDTO user;
        if (id != null) {
            if (!Utility.isInteger(id)) return INDEX_PAGE;
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
        List<Experience> listExperience = experienceService.getAllExperienceByProfileID(mentor.getProfileId());
        model.addAttribute("listExperience", listExperience);
    }

    public void addStatusAttribute(Model model, UserDTO target, UserDTO currentUser) {
        Integer statusRequest = (requestService.findStatusRequest(target.getId(), currentUser.getId()) != null) ? 1 : 0;
        Integer statusConnect = (connectService.findConnectByUser1AndUser2(target.getId(), currentUser.getId()) != null ||
                connectService.findConnectByUser1AndUser2(currentUser.getId(), target.getId()) != null) ? 1 : 0;
        model.addAttribute("statusConnect", statusConnect);
        model.addAttribute("statusRequest", statusRequest);
    }
}
