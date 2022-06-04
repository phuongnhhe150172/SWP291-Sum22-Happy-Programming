package swp.happyprogramming.controllers.mentor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import swp.happyprogramming.dto.*;
import swp.happyprogramming.model.Experience;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.services.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MentorProfileController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IMentorService mentorService;

    @Autowired
    private IMenteeService menteeService;

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

    @GetMapping("/mentee/view")
    public String viewMentorProfile(Model model, @RequestParam(value = "idOr", required = false) String idOr,
                                    @RequestParam(value = "idEe", required = false) String idEe) {
        try {
            long mentorId = Integer.parseInt(idOr);
            long menteeId = Integer.parseInt(idEe);
            MentorDTO mentorDTO = mentorService.findMentor(mentorId);
            MenteeDTO menteeDTO = menteeService.findMentee(menteeId);
            Integer status = userService.statusRequest(mentorDTO.getId(), menteeDTO.getId());

            model.addAttribute("mentor", mentorDTO);
            model.addAttribute("mentee", menteeDTO);
            model.addAttribute("status", status);
            return "mentee/view/mentorProfile";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @GetMapping("/mentor/profile/{id}")
    public String viewProfilePublic(Model model, @PathVariable String id) {
        try {
            long mentorId = Integer.parseInt(id);
            MentorDTO mentorDTO = mentorService.findMentor(mentorId);
            if (mentorDTO == null) return "redirect:index";
            model.addAttribute("mentor", mentorDTO);
            return "mentor/profile/view";
        } catch (NumberFormatException e) {
            return "redirect:/index";
        }
    }

    @GetMapping("/mentor/profile/update")
    public String updateProfileMentor(Model model, @RequestParam(value = "id", required = false) String id) {
        try {
            long mentorId = Integer.parseInt(id);

            MentorDTO mentorDTO = mentorService.findMentor(mentorId);
            long wardId = wardService.getWardIdByAddressId(mentorDTO.getAddressId());
            long districtId = districtService.getDistrictIdByWardId(wardId);
            long provinceId = provinceService.getProvinceIdByDistrictId(districtId);

            List<ProvinceDTO> listProvinces = provinceService.findAllProvinces();
            List<DistrictDTO> listDistrict = districtService.findAllDistrict(provinceId);
            List<WardDTO> listWard = wardService.findAllWard(districtId);

            ArrayList<Experience> listExperience = experienceService.getAllExperienceByProfileID(mentorDTO.getProfileId());
            List<Skill> listSkill = skillService.getAllSkill();
            Map<Skill, Integer> mapSkill = mentorService.findMapSkill(listSkill, mentorDTO.getSkills());

            model.addAttribute("mentor", mentorDTO);
            model.addAttribute("mentorId", mentorId);

            model.addAttribute("wardId", wardId);
            model.addAttribute("districtId", districtId);
            model.addAttribute("provinceId", provinceId);

            model.addAttribute("listProvinces", listProvinces);
            model.addAttribute("listDistrict", listDistrict);
            model.addAttribute("listWard", listWard);
            model.addAttribute("listExperience", listExperience);
            model.addAttribute("mapSkill", mapSkill);

            return "mentor/profile/update";
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
        try {
            long mentorId = Integer.parseInt(id);
            MentorDTO mentorDTO = mentorService.findMentor(mentorId);

            model.addAttribute("mentor", mentorDTO);
            return "mentor/profile/view";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }
}
