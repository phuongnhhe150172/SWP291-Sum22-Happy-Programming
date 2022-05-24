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
import swp.happyprogramming.services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MentorProfileController {
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

    @GetMapping("/mentor/profile/{id}")
    public String getProfile(Model model, @PathVariable String id) {
        try {
            long mentorId = Integer.parseInt(id);
            MentorDTO mentorDTO = mentorService.findMentor(mentorId);
            if (mentorDTO == null) {
                return "redirect:index";
            }
            model.addAttribute("mentor", mentorDTO);
            return "profile";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @GetMapping("/mentor/profile/update")
    public String updateProfileMentor(Model model, @RequestParam(value = "id", required = false) String id) {
        try {
            long mentorId = Integer.parseInt(id);

            MentorDTO mentorDTO = mentorService.findMentor(mentorId);
            long wardId = wardService.getWardIdByProfileId(mentorDTO.getProfileId());
            long districtId = districtService.getDistrictIdByWardId(wardId);
            long provinceId = provinceService.getProvinceIdByDistrictId(districtId);

            List<ProvinceDTO> listProvinces = provinceService.findAllProvinces();
            List<DistrictDTO> listDistrict = districtService.findAllDistrict(provinceId);
            List<WardDTO> listWard = wardService.findAllWard(districtId);

            ArrayList<Experience> listExperience = experienceService.getAllExperienceByProfileID(mentorDTO.getProfileId());

            model.addAttribute("mentor", mentorDTO);
            model.addAttribute("listProvinces", listProvinces);
            model.addAttribute("mentorId", mentorId);
            model.addAttribute("wardId", wardId);
            model.addAttribute("districtId", districtId);
            model.addAttribute("provinceId", provinceId);
            model.addAttribute("listDistrict", listDistrict);
            model.addAttribute("listWard", listWard);
            model.addAttribute("listExperience",listExperience);

            return "mentor/profile/update";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @PostMapping("/mentor/profile/update")
    public String updateProfileMentor(@ModelAttribute("mentor") MentorDTO mentor,
                                      @RequestParam Map<String, Object> params,
                                      @RequestParam("experieceValue") List<String> experieceValue) {
        try {
            long mentorId = Integer.parseInt(String.valueOf(params.get("mentorId")));
            long wardId = Integer.parseInt(String.valueOf(params.get("wardId")));

            mentorService.updateMentor(mentorId, mentor, wardId, experieceValue);

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

            return "mentor/profile/area/district";
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

            return "mentor/profile/area/ward";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @GetMapping("/mentor/profile/view")
    public String viewProfileMentor(Model model, @RequestParam(value = "id", required = false) String id) {
        try {
            long mentorId = Integer.parseInt(id);
            MentorDTO mentorDTO = mentorService.findMentor(mentorId);
            ArrayList<Experience> listExperience = experienceService.getAllExperienceByProfileID(mentorDTO.getProfileId());

            model.addAttribute("mentor", mentorDTO);
            model.addAttribute("listExperience",listExperience);
            return "mentor/profile/view";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }
}
