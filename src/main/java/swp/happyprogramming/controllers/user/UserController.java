package swp.happyprogramming.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.dto.DistrictDTO;
import swp.happyprogramming.dto.ProvinceDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.dto.WardDTO;
import swp.happyprogramming.services.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
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

    @GetMapping("/profile")
    public String showUserProfile(Model model, @RequestParam(value = "id", required = false) String id) {
        if (id != null) {
            long userId = Integer.parseInt(id);
        } else {
            UserDTO user = userService.findUser((UserDTO) session.getAttribute("userInformation"));

            String address = addressService.getAddress(user.getAddressId());

            model.addAttribute("user", user);
            model.addAttribute("address", address);
        }
        return "user/user-profile";
    }


    @GetMapping("/update")
    public String updateUserProfile(Model model) {
        UserDTO user = userService.findUser((UserDTO) session.getAttribute("userInformation"));

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
}
