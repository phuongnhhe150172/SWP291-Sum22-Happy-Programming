package swp.happyprogramming.application.usecase;

import java.util.List;
import swp.happyprogramming.adapter.dto.DistrictDTO;
import swp.happyprogramming.adapter.dto.ProvinceDTO;
import swp.happyprogramming.adapter.dto.WardDTO;
import swp.happyprogramming.domain.model.Address;

public interface IAddressService {
    String getAddress(long addressId);

  List<WardDTO> findAllWard(long districtId);

  long getWardIdByAddressId(long addressId);

  List<DistrictDTO> findAllDistrict(long provinceId);

  long getDistrictIdByWardId(long wardId);

  List<ProvinceDTO> findAllProvinces();

  long getProvinceIdByDistrictId(long districtId);

  Address createNewAddress();

  //    delete address
  void deleteAddress(long addressId);

  //  save address
  void saveAddress(Address address);
}
