package swp.happyprogramming.adapter.port.in.address;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swp.happyprogramming.application.dto.DistrictDTO;
import swp.happyprogramming.application.dto.WardDTO;
import swp.happyprogramming.application.port.usecase.IAddressService;

@Controller
public class AddressController {

  @Autowired
  private IAddressService addressService;

  @GetMapping("/update/district")
  public String updateDistrictByProvinceId(Model model,
    @RequestParam(value = "provinceId", required = false) String provinceId,
    @RequestParam(value = "districtId", required = false) String districtId) {
    try {
      long province = Integer.parseInt(provinceId);
      long district = Integer.parseInt(districtId);

      List<DistrictDTO> listDistrict = addressService.findAllDistrict(province);
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

      List<WardDTO> listWard = addressService.findAllWard(district);
      model.addAttribute("listWard", listWard);
      model.addAttribute("war", ward);

      return "components/area/ward";
    } catch (NumberFormatException e) {
      return "redirect:index";
    }
  }
}
