package swp.happyprogramming.controllers.mentor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import swp.happyprogramming.dto.DistrictDTO;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.dto.ProvinceDTO;
import swp.happyprogramming.dto.WardDTO;
import swp.happyprogramming.services.*;
import swp.happyprogramming.services.servicesimpl.DistrictService;
import swp.happyprogramming.services.servicesimpl.MentorService;
import swp.happyprogramming.services.servicesimpl.ProvinceService;
import swp.happyprogramming.services.servicesimpl.WardService;

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
            List<ProvinceDTO> listProvinces = provinceService.findAllProvinces();
            long wardId = wardService.getWardIdByProfileId(mentorDTO.getProfileId());
            long districtId = districtService.getDistrictIdByWardId(wardId);
            long provinceId = provinceService.getProvinceIdByDistrictId(districtId);

            List<DistrictDTO> listDistrict = districtService.findAllDistrict(provinceId);
            List<WardDTO> listWard = wardService.findAllWard(districtId);

            model.addAttribute("mentor", mentorDTO);
            model.addAttribute("listProvinces", listProvinces);
            model.addAttribute("mentorId",mentorId);
            model.addAttribute("wardId",wardId);
            model.addAttribute("districtId",districtId);
            model.addAttribute("provinceId",provinceId);
            model.addAttribute("listDistrict", listDistrict);
            model.addAttribute("listWard",listWard);

            return "mentor/profile/update";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @PostMapping("/mentor/profile/update")
    public String updateProfileMentor(Model model,@ModelAttribute("mentor") MentorDTO mentor,
                                      @RequestParam Map<String,String> params){
        try{
            long mentorId = Integer.parseInt(params.get("mentorId"));
            long wardId = Integer.parseInt(params.get("wardId"));
            mentorService.updateMentor(mentorId,mentor,wardId);

            return "redirect:view?id=" + String.valueOf(mentorId);
        }catch (NumberFormatException e){
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
            model.addAttribute("dis",district);

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
            model.addAttribute("listWard",listWard);
            model.addAttribute("war",ward);

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
            model.addAttribute("mentor", mentorDTO);
            return "mentor/profile/view";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }
}
