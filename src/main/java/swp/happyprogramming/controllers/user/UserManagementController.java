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

import javax.servlet.http.HttpSession;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        if (sessionUser == null) {
            return "redirect:/login";
        }
        UserDTO user;
        if (id != null) {
            long userId = Integer.parseInt(id);
            user = userService.findUser(userId);
            UserDTO userDTO = (UserDTO) sessionUser;

            Integer statusRequest = (requestService.findStatusRequest(userDTO.getId(), user.getId()) != null) ? 1 : 0;
            Integer statusConnect = (connectService.findConnectByUser1AndUser2(user.getId(), userDTO.getId()) != null ||
                    connectService.findConnectByUser1AndUser2(userDTO.getId(), user.getId()) != null) ? 1 : 0;
            model.addAttribute("statusConnect", statusConnect);
            model.addAttribute("statusRequest", statusRequest);
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
        //      Hoàng Văn Nam -   - Update profile mentee
        UserDTO user;
        if (id != null) {
            long userId = Integer.parseInt(id);
            user = userService.findUser(userId);
        } else {
            user = (UserDTO) session.getAttribute(USER_SESSION);
        }
        long districtId = user.getAddress().getDistrict().getId();
        long provinceId = user.getAddress().getProvince().getId();

        List<ProvinceDTO> listProvinces = provinceService.findAllProvinces();
        List<DistrictDTO> listDistrict = districtService.findAllDistrict(provinceId);
        List<WardDTO> listWard = wardService.findAllWard(districtId);

        model.addAttribute("user", user);
        model.addAttribute("listProvinces", listProvinces);
        model.addAttribute("listDistrict", listDistrict);
        model.addAttribute("listWard", listWard);
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
                Integer statusRequest = (requestService.findStatusRequest(mentor.getId(), user.getId()) != null) ? 1 : 0;
                Integer statusConnect = (connectService.findConnectByUser1AndUser2(user.getId(), mentor.getId()) != null ||
                        connectService.findConnectByUser1AndUser2(mentor.getId(), user.getId()) != null) ? 1 : 0;
                model.addAttribute("statusConnect", statusConnect);
                model.addAttribute("statusRequest", statusRequest);
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
            long districtId = mentorDTO.getAddress().getDistrict().getId();
            long provinceId = mentorDTO.getAddress().getProvince().getId();

            List<ProvinceDTO> listProvinces = provinceService.findAllProvinces();
            List<DistrictDTO> listDistrict = districtService.findAllDistrict(provinceId);
            List<WardDTO> listWard = wardService.findAllWard(districtId);

            ArrayList<Experience> listExperience = experienceService.getAllExperienceByProfileID(mentorDTO.getProfileId());
            List<Skill> listSkill = skillService.getAllSkill();
            Map<Skill, Integer> mapSkill = mentorService.findMapSkill(listSkill, mentorDTO.getSkills());

            model.addAttribute("mentor", mentorDTO);
            model.addAttribute("mentorId", mentorId);

            model.addAttribute("listProvinces", listProvinces);
            model.addAttribute("listDistrict", listDistrict);
            model.addAttribute("listWard", listWard);
            model.addAttribute("listExperience", listExperience);
            model.addAttribute("mapSkill", mapSkill);

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

            mentorService.updateMentor(mentor, wardId, experieceValue, skillValue);

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
        UserDTO user = userService.findUser(userDTO.getId());
        session.setAttribute(USER_SESSION, user);
        return "redirect:profile";
    }

    @GetMapping("/create")
    public String createCv(Model model,@RequestParam(value = "id", required = false) String id){
        UserDTO user;
        try{
            if (id != null) {
                long userId = Integer.parseInt(id);
                user = userService.findUser(userId);
            } else {
                user = (UserDTO) session.getAttribute(USER_SESSION);
            }
            List<Skill> listSkill = skillService.getAllSkill();

            model.addAttribute("listSkill",listSkill);
            model.addAttribute("user",user);
            return "user/createcv";
        }catch (NumberFormatException e){
            return INDEX_PAGE;
        }
    }

    @PostMapping("/create")
    public String createCv(@RequestParam(value = "id", required = false) String id,
                           @RequestParam(value = "experieceValue", required = false) List<String> experieceValue,
                           @RequestParam(value = "skillValue", required = false) List<String> skillValue){
        try {
            long userId = Integer.parseInt(id);

            mentorService.createCv(userId,experieceValue,skillValue);
            return "redirect:cv";
        }catch (NumberFormatException e){
            return INDEX_PAGE;
        }
    }
}
