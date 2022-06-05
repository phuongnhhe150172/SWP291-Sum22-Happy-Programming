package swp.happyprogramming.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.dto.*;
import swp.happyprogramming.repository.IAddressRepository;
import swp.happyprogramming.services.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class UserManagementController {
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
    private IAddressService addressService;
    @Autowired
    private IMentorService mentorService;

    @GetMapping("/profile")
    public String showUserProfile(Model model,
                                  @RequestParam(value = "id", required = false) String id) {
        UserDTO user;
        if (id != null) {
            long userId = Integer.parseInt(id);
            user = userService.findUser(userId);
        } else {
            user = userService.findUser((UserDTO) session.getAttribute("userInformation"));
        }
        String address = addressService.getAddress(user.getAddressId());
        model.addAttribute("user", user);
        model.addAttribute("address", address);
        return "user/user-profile";
    }

    @GetMapping("/update")
    public String updateUserProfile(Model model, @RequestParam(value = "id", required = false) String id) {
        UserDTO user;
        if (id != null) {
            long userId = Integer.parseInt(id);
            user = userService.findUser(userId);
        } else {
            user = userService.findUser((UserDTO) session.getAttribute("userInformation"));
        }
        long wardId = wardService.getWardIdByAddressId(user.getAddressId());
        long districtId = districtService.getDistrictIdByWardId(wardId);
        long provinceId = provinceService.getProvinceIdByDistrictId(districtId);

        List<ProvinceDTO> listProvinces = provinceService.findAllProvinces();
        List<DistrictDTO> listDistrict = districtService.findAllDistrict(provinceId);
        List<WardDTO> listWard = wardService.findAllWard(districtId);

        model.addAttribute("user", user);
        model.addAttribute("listProvinces", listProvinces);
        model.addAttribute("listDistrict", listDistrict);
        model.addAttribute("listWard", listWard);
        model.addAttribute("wardId", wardId);
        model.addAttribute("districtId", districtId);
        model.addAttribute("provinceId", provinceId);
        return "user/update-profile";
    }

    @GetMapping("/mentors")
    public String allMentors(Model model) {
        List<MentorDTO> mentors = mentorService.getMentors();
        model.addAttribute("mentors", mentors);
        return "mentor/all-mentors";
    }

    @PostMapping("/update")
    public String updateUserProfile(@ModelAttribute("user") UserDTO userDTO,
                                    @RequestParam Map<String, Object> params) {
        try {
            long wardId = Integer.parseInt(String.valueOf(params.get("wardId")));
            userService.updateUserProfile(userDTO, wardId);
            // Update session
            session.setAttribute("userInformation", userDTO);
            return "redirect:profile";
        } catch (NumberFormatException e) {
            return "redirect:index";
        }

    }

    @GetMapping("/update/district")
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

    @GetMapping("/update/ward")
    public String updateWardByDistrictId(Model model,
                                         @RequestParam(value = "districtId", required = false) String districtId,
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
}
