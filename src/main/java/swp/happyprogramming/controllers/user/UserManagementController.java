package swp.happyprogramming.controllers.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swp.happyprogramming.dto.DistrictDTO;
import swp.happyprogramming.dto.ProvinceDTO;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.dto.WardDTO;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.*;

import javax.servlet.http.HttpSession;
import java.util.List;

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
    private IExperienceService experienceService;

    @Autowired
    private ISkillService skillService;

    @GetMapping("/user-profile")
    public String showUserProfile(Model model){
        UserDTO user = userService.findUser();

        model.addAttribute("user",user);
        return "user/user-profile";
    }

    @GetMapping("/update-profile")
    public String updateUserProfile(Model model){
        UserDTO user = userService.findUser();

        long wardId = wardService.getWardIdByAddressId(user.getAddressId());
        long districtId = districtService.getDistrictIdByWardId(wardId);
        long provinceId = provinceService.getProvinceIdByDistrictId(districtId);

        List<ProvinceDTO> listProvinces = provinceService.findAllProvinces();
        List<DistrictDTO> listDistrict = districtService.findAllDistrict(provinceId);
        List<WardDTO> listWard = wardService.findAllWard(districtId);

        model.addAttribute("user",user);
        model.addAttribute("listProvinces",listProvinces);
        model.addAttribute("listDistrict",listDistrict);
        model.addAttribute("listWard",listWard);
        model.addAttribute("wardId", wardId);
        model.addAttribute("districtId", districtId);
        model.addAttribute("provinceId", provinceId);
        return "user/update-profile";
    }
}
