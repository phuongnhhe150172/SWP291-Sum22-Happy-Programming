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
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class UserManagementController {
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

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

    @GetMapping("/profile")
    public String showUserProfile(Model model,
                                  @RequestParam(value = "id", required = false) String id) {
        //        Nguyễn Huy Hoàng - 04 - View public mentor profile
        UserDTO user;
        if (id != null) {
            long userId = Integer.parseInt(id);
            user = userService.findUser(userId);
        } else {
            Object sessionUser = session.getAttribute("userInformation");
            if (sessionUser == null) {
                return "redirect:/login";
            }
            user = (UserDTO) sessionUser;
        }
        String role = (String) session.getAttribute("role");
        model.addAttribute("user", user);
        model.addAttribute("role", role);
        return "user/user-profile";
    }

    @GetMapping("/update")
    public String updateUserProfile(Model model, @RequestParam(value = "id", required = false) String id) {
        UserDTO user;
        if (id != null) {
            long userId = Integer.parseInt(id);
            user = userService.findUser(userId);
        } else {
            user = (UserDTO) session.getAttribute("userInformation");
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
        try {
            long wardId = Integer.parseInt(String.valueOf(params.get("wardId")));
            UserDTO user = userService.updateUserProfile(userDTO, wardId);
            // Update session
            session.setAttribute("userInformation", user);
            return "redirect:profile";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }

    }

    @GetMapping("/cv")
    public String viewMentorCV(Model model, @RequestParam(value = "id", required = false) String id) {
        try {
            long mentorId = Integer.parseInt(id);
            MentorDTO mentor = mentorService.findMentor(mentorId);
            model.addAttribute("mentor", mentor);
            return "user/mentorcv";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @GetMapping("/update/cv")
    public String updateMentorCv(Model model, @RequestParam(value = "id", required = false) String id) {
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
            return "redirect:/index";
        }
    }

    @PostMapping("/update/cv")
    public String updateMentorCv(@ModelAttribute("mentor") MentorDTO mentor,
                                 @RequestParam Map<String, Object> params,
                                 @RequestParam(value = "experieceValue", required = false) List<String> experieceValue,
                                 @RequestParam(value = "skillValue", required = false) List<String> skillValue) {
        try {
            long wardId = Integer.parseInt(String.valueOf(params.get("wardId")));

            mentorService.updateMentor(mentor, wardId, experieceValue, skillValue);

            return "redirect:../cv?id=" + mentor.getId();
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @PostMapping("/uploading")
    public String updateImage(@RequestParam("image") MultipartFile image){
        UserDTO userDTO =(UserDTO) session.getAttribute("userInformation");
        userService.updateImage(userDTO.getId(),CURRENT_FOLDER,image);
        UserDTO user = userService.findUser(userDTO.getId());
        session.setAttribute("userInformation", user);
        return "redirect:profile";
    }
}
