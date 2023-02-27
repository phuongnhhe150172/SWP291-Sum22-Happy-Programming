package swp.happyprogramming.application.port.usecase;

import java.util.List;
import swp.happyprogramming.application.dto.DistrictDTO;
import swp.happyprogramming.application.dto.ProvinceDTO;
import swp.happyprogramming.application.dto.WardDTO;
import swp.happyprogramming.domain.model.Address;
import swp.happyprogramming.domain.model.User;

public interface IAddressService {

  String getAddress(long addressId);

  List<WardDTO> findAllWard(long districtId);

  long getWardIdByAddressId(long addressId);

  List<DistrictDTO> findAllDistrict(long provinceId);

  long getDistrictIdByWardId(long wardId);

  List<ProvinceDTO> findAllProvinces();

  long getProvinceIdByDistrictId(long districtId);

  void updateAddress(User user, long wardId, String name);

  Address createNewAddress();

  //    delete address
  void deleteAddress(long addressId);

  //  save address
  void saveAddress(Address address);
}
