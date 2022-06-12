package swp.happyprogramming.controllers.mentee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import swp.happyprogramming.dto.*;
import swp.happyprogramming.model.Experience;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MenteeProfileController {

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

//    @Autowired
//    private IExperienceService experienceService;
//
//    @Autowired
//    private ISkillService skillService;

    @GetMapping("/mentee/profile/{id}")
    public String viewProfilePublic(Model model, @PathVariable String id) {
        try {
            long menteeId = Integer.parseInt(id);
            MenteeDTO menteeDTO = menteeService.findMentee(menteeId);
            if (menteeDTO == null) return "redirect:index";
            model.addAttribute("mentee", menteeDTO);
            return "profile";
        } catch (NumberFormatException e) {
            return "redirect:/index";
        }
    }

    @GetMapping("/mentee/profile/update")
    public String updateProfileMentee(Model model, @RequestParam(value = "id", required = false) String id) {
        try {
            long menteeId = Integer.parseInt(id);

            MenteeDTO menteeDTO = menteeService.findMentee(menteeId);
            long wardId = wardService.getWardIdByProfileId(menteeDTO.getProfileId());
            long districtId = districtService.getDistrictIdByWardId(wardId);
            long provinceId = provinceService.getProvinceIdByDistrictId(districtId);

            List<ProvinceDTO> listProvinces = provinceService.findAllProvinces();
            List<DistrictDTO> listDistrict = districtService.findAllDistrict(provinceId);
            List<WardDTO> listWard = wardService.findAllWard(districtId);

            model.addAttribute("mentee", menteeDTO);
            model.addAttribute("menteeId", menteeId);

            model.addAttribute("wardId", wardId);
            model.addAttribute("districtId", districtId);
            model.addAttribute("provinceId", provinceId);

            model.addAttribute("listProvinces", listProvinces);
            model.addAttribute("listDistrict", listDistrict);
            model.addAttribute("listWard", listWard);

            return "mentee/profile/update";
        } catch (NumberFormatException e) {
            return "redirect:/index";
        }
    }

    @PostMapping("/mentee/profile/update")
    public String updateProfileMentee(@ModelAttribute("mentee") MenteeDTO mentee,
                                      @RequestParam Map<String, Object> params) {
        try {
            long menteeId = Integer.parseInt(String.valueOf(params.get("menteeId")));
            long wardId = Integer.parseInt(String.valueOf(params.get("wardId")));
            long wa = Integer.parseInt(String.valueOf(params.get("wa")));

            menteeService.updateMentee(menteeId, mentee, wardId, wa);

            return "redirect:view?id=" + menteeId;
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @GetMapping("/mentee/profile/update/district")
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

    @GetMapping("/mentee/profile/update/ward")
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

    @GetMapping("/mentee/profile/view")
    public String viewMenteeProfilePrivate(Model model, @RequestParam(value = "id", required = false) String id) {
        try {
            long menteeId = Integer.parseInt(id);
            MenteeDTO menteeDTO = menteeService.findMentee(menteeId);

            model.addAttribute("mentee", menteeDTO);
            return "mentee/profile/view";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }
}
