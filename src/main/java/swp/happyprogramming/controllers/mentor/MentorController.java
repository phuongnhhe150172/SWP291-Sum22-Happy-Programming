package swp.happyprogramming.controllers.mentor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import swp.happyprogramming.dto.DistrictDTO;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.dto.ProvinceDTO;
import swp.happyprogramming.dto.WardDTO;
import swp.happyprogramming.model.Experience;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.services.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MentorController {
    @Autowired
    private IMentorService mentorService;

    @Autowired
    private IProvinceService provinceService;

    @Autowired
    private IDistrictService districtService;

    @Autowired
    private IWardService wardService;

    @Autowired
    private IExperienceService experienceService;

    @Autowired
    private ISkillService skillService;

    @GetMapping("/mentors")
    public String mentors(Model model) {
        List<MentorDTO> mentors = mentorService.getMentors();
        model.addAttribute("mentors", mentors);
        return "mentor/all-mentors";
    }

    @GetMapping("/mentor/profile/{id}")
    public String viewProfilePublic(Model model, @PathVariable String id) {
        UserDTO user = (UserDTO) session.getAttribute("userInformation");
        try {
            long mentorId = Integer.parseInt(id);
            MentorDTO mentorDTO = mentorService.findMentor(user);
            if (mentorDTO == null) return "redirect:index";
            model.addAttribute("user", mentorDTO);
            return "user/user-profile";
        } catch (NumberFormatException e) {
            return "redirect:/index";
        }
    }

    @GetMapping("/mentor/profile/update")
    public String updateProfileMentor(Model model, @RequestParam(value = "id", required = false) String id) {
        UserDTO user = (UserDTO) session.getAttribute("userInformation");
        try {
            long mentorId = Integer.parseInt(id);

            MentorDTO mentorDTO = mentorService.findMentor(user);
            long wardId = wardService.getWardIdByAddressId(mentorDTO.getAddressId());
            long districtId = districtService.getDistrictIdByWardId(wardId);
            long provinceId = provinceService.getProvinceIdByDistrictId(districtId);

            List<ProvinceDTO> listProvinces = provinceService.findAllProvinces();
            List<DistrictDTO> listDistrict = districtService.findAllDistrict(provinceId);
            List<WardDTO> listWard = wardService.findAllWard(districtId);

            ArrayList<Experience> listExperience = experienceService.getAllExperienceByProfileID(mentorDTO.getProfileId());
            List<Skill> listSkill = skillService.getAllSkill();
            Map<Skill, Integer> mapSkill = mentorService.findMapSkill(listSkill, mentorDTO.getSkills());

            model.addAttribute("user", mentorDTO);

            model.addAttribute("wardId", wardId);
            model.addAttribute("districtId", districtId);
            model.addAttribute("provinceId", provinceId);

            model.addAttribute("listProvinces", listProvinces);
            model.addAttribute("listDistrict", listDistrict);
            model.addAttribute("listWard", listWard);
            model.addAttribute("listExperience", listExperience);
            model.addAttribute("mapSkill", mapSkill);

            return "user/update-profile";
        } catch (NumberFormatException e) {
            return "redirect:/index";
        }
    }

    @PostMapping("/mentor/profile/update")
    public String updateProfileMentor(@ModelAttribute("mentor") MentorDTO mentor,
                                      @RequestParam Map<String, Object> params,
                                      @RequestParam(value = "experieceValue", required = false) List<String> experieceValue,
                                      @RequestParam(value = "skillValue", required = false) List<String> skillValue) {
        try {
            long mentorId = Integer.parseInt(String.valueOf(params.get("mentorId")));
            long wardId = Integer.parseInt(String.valueOf(params.get("wardId")));
            long wa = Integer.parseInt(String.valueOf(params.get("wa")));

            mentorService.updateMentor(mentorId, mentor, wardId, wa, experieceValue, skillValue);

            return "redirect:view?id=" + mentorId;
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @GetMapping("/mentor/profile/update/district")
    public String updateDistrictByProvinceId(Model model, @RequestParam(value = "provinceId", required = false) String provinceId,
                                             @RequestParam(value = "districtId", required = false) String districtId) {
        try {
            long province = Integer.parseInt(provinceId);
            long district = Integer.parseInt(districtId);

            List<DistrictDTO> listDistrict = districtService.findAllDistrict(province);
            model.addAttribute("listDistrict", listDistrict);
            model.addAttribute("dis", district);

            return "components/area/district";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @GetMapping("/mentor/profile/update/ward")
    public String updateWardByDistrictId(Model model, @RequestParam(value = "districtId", required = false) String districtId,
                                         @RequestParam(value = "wardId", required = false) String wardId) {
        try {
            long district = Integer.parseInt(districtId);
            long ward = Integer.parseInt(wardId);

            List<WardDTO> listWard = wardService.findAllWard(district);
            model.addAttribute("listWard", listWard);
            model.addAttribute("war", ward);

            return "components/area/ward";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @GetMapping("/mentor/profile/view")
    public String viewMentorProfilePrivate(Model model, @RequestParam(value = "id", required = false) String id) {
        UserDTO user = (UserDTO) session.getAttribute("userInformation");
        try {
            long mentorId = Integer.parseInt(id);
            MentorDTO mentorDTO = mentorService.findMentor(user);

            model.addAttribute("mentor", mentorDTO);
            return "mentor/profile/view";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }
}
