package swp.happyprogramming.controllers.mentee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import swp.happyprogramming.dto.*;
import swp.happyprogramming.services.servicesimpl.*;

import java.util.List;
import java.util.Map;

@Controller
public class MenteeProfileController {

    @Autowired
    private MenteeService menteeService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private WardService wardService;

    @GetMapping("/mentees")
    public String mentees(Model model) {
        List<MenteeDTO> mentees = menteeService.getAllMentees();
        model.addAttribute("mentees", mentees);
        return "mentee/all-mentees";
    }

    @GetMapping("/mentee/profile/{id}")
    public String getProfile(WebRequest request, Model model, @PathVariable String id) {
        try {
            long menteeId = Integer.parseInt(id);
            MenteeDTO menteeDTO = menteeService.findMentee(menteeId);
            model.addAttribute("mentee", menteeDTO);
            return "mentee/profile/view";
        } catch (NumberFormatException e) {
            return "redirect:/index";
        }
    }

    @GetMapping("/mentee/profile/update")
    public String updateProfileMentee(Model model, @RequestParam(value = "id", required = false) String id) {
        try {
            long menteeId = Integer.parseInt(id);

            MenteeDTO menteeDTO = menteeService.findMentee(menteeId);
            List<ProvinceDTO> listProvinces = provinceService.findAllProvinces();
            long wardId = wardService.getWardIdByAddressId(menteeDTO.getAddressId());
            long districtId = districtService.getDistrictIdByWardId(wardId);
            long provinceId = provinceService.getProvinceIdByDistrictId(districtId);

            List<DistrictDTO> listDistrict = districtService.findAllDistrict(provinceId);
            List<WardDTO> listWard = wardService.findAllWard(districtId);

            model.addAttribute("mentee", menteeDTO);
            model.addAttribute("listProvinces", listProvinces);
            model.addAttribute("mentorId",menteeId);
            model.addAttribute("wardId",wardId);
            model.addAttribute("districtId",districtId);
            model.addAttribute("provinceId",provinceId);
            model.addAttribute("listDistrict", listDistrict);
            model.addAttribute("listWard",listWard);

            return "mentee/profile/update";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @PostMapping("/mentee/profile/update")
    public String updateProfileMentee(Model model,@ModelAttribute("mentee") MenteeDTO mentee,
                                      @RequestParam Map<String,String> params){
        try{
            long menteeId = Integer.parseInt(params.get("menteeId"));
            long wardId = Integer.parseInt(params.get("wardId"));
            menteeService.updateMentee(menteeId,mentee,wardId);

            return "redirect:view?id=" + menteeId;
        }catch (NumberFormatException e){
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
            model.addAttribute("dis",district);

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
            model.addAttribute("listWard",listWard);
            model.addAttribute("war",ward);

            return "components/area/ward";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @GetMapping("/mentee/profile/view")
    public String viewProfileMentee(Model model, @RequestParam(value = "id", required = false) String id) {
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
