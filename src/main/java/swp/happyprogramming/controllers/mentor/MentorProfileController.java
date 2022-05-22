package swp.happyprogramming.controllers.mentor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
            model.addAttribute("mentor", mentorDTO);
            return "profile";
        } catch (NumberFormatException e) {
            return "redirect:/index";
        }
    }

    @GetMapping("/mentor/profile/update")
    public String updateProfileMentor(Model model, @RequestParam(value = "id", required = false) String id) {
        try {
            long mentorId = Integer.parseInt(id);
            MentorDTO mentorDTO = mentorService.findMentor(mentorId);
            List<ProvinceDTO> listProvinces = provinceService.findAllProvinces();
            model.addAttribute("mentor", mentorDTO);
            model.addAttribute("listProvinces", listProvinces);
            return "mentor/profile/update";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @GetMapping("/mentor/profile/update/district")
    public String updateDistrictByProvinceId(Model model, @RequestParam(value = "provinceId", required = false) String provinceId) {
        try {
            long id = Integer.parseInt(provinceId);
            List<DistrictDTO> listDistrict = districtService.findAllDistrict(id);
            model.addAttribute("listDistrict", listDistrict);
            return "mentor/profile/area/district";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }

    @GetMapping("/mentor/profile/update/ward")
    public String updateWardByDistrictId(Model model, @RequestParam(value = "districtId", required = false) String districtId) {
        try {
            long id = Integer.parseInt(districtId);
            List<WardDTO> listWard = wardService.findAllWard(id);
            model.addAttribute("listWard", listWard);
            return "mentor/profile/area/ward";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }
    }
}