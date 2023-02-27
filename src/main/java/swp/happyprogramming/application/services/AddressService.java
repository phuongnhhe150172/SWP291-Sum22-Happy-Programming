package swp.happyprogramming.application.services;

import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import swp.happyprogramming.application.dto.DistrictDTO;
import swp.happyprogramming.application.dto.ProvinceDTO;
import swp.happyprogramming.application.dto.WardDTO;
import swp.happyprogramming.application.port.out.AddressPortOut;
import swp.happyprogramming.application.port.out.DistrictPortOut;
import swp.happyprogramming.application.port.out.ProvincePortOut;
import swp.happyprogramming.application.port.out.WardPortOut;
import swp.happyprogramming.application.port.usecase.IAddressService;
import swp.happyprogramming.domain.model.Address;
import swp.happyprogramming.domain.model.District;
import swp.happyprogramming.domain.model.Province;
import swp.happyprogramming.domain.model.User;
import swp.happyprogramming.domain.model.Ward;
import swp.happyprogramming.utility.Utility;

@AllArgsConstructor
public class AddressService implements IAddressService {

  private AddressPortOut addressRepository;
  private WardPortOut wardRepository;
  private DistrictPortOut districtRepository;
  private ProvincePortOut provinceRepository;

  @Override
  public String getAddress(long addressId) {
    Address address = addressRepository.findByAddressId(addressId);
    Ward ward = wardRepository.findWardById(address.getWard().getId());
    District district = districtRepository.findDistrictById(
      ward.getDistrict().getId());
    Province province = provinceRepository.findProvinceById(
      district.getProvince().getId());
    return address.getName() + ", " + ward.getName() + ", " + district.getName()
      + ", "
      + province.getName();
  }

  @Override
  public List<WardDTO> findAllWard(long districtId) {
    ModelMapper mapper = new ModelMapper();
    List<Ward> list = wardRepository.findAllByDistrictId(districtId);
    return Utility.mapList(list,
      (Ward ward) -> mapper.map(ward, WardDTO.class));
  }

  @Override
  public long getWardIdByAddressId(long addressId) {
    Address address = addressRepository.findByAddressId(addressId);
    return address.getWard().getId();
  }

  @Override
  public List<DistrictDTO> findAllDistrict(long provinceId) {
    ModelMapper mapper = new ModelMapper();
    List<District> list = districtRepository.findAllByProvinceId(provinceId);
    return Utility.mapList(list,
      (District district) -> mapper.map(district, DistrictDTO.class));
  }

  @Override
  public long getDistrictIdByWardId(long wardId) {
    Ward ward = wardRepository.findById(wardId).orElse(new Ward());
    return ward.getDistrict().getId();
  }

  @Override
  public List<ProvinceDTO> findAllProvinces() {
    ModelMapper mapper = new ModelMapper();
    List<Province> list = provinceRepository.findAll();
    return Utility.mapList(list,
      (Province province) -> mapper.map(province, ProvinceDTO.class));
  }

  @Override
  public long getProvinceIdByDistrictId(long districtId) {
    District district = districtRepository.findById(districtId)
      .orElse(new District());
    return district.getProvince().getId();
  }

  @Override
  public void updateAddress(User user, long wardId, String name) {
    Address address = user.getAddress();

    Ward ward = wardRepository.findById(wardId).orElse(null);
    address.setWard(ward);
    address.setName(name);

    user.setAddress(address);
  }

  @Override
  public Address createNewAddress() {
    Address address = new Address();
    Ward firstWard = wardRepository.findFirst();
    address.setWard(firstWard);
    return addressRepository.save(address);
  }

  @Override
  public void deleteAddress(long addressId) {
    addressRepository.deleteById(addressId);
  }

  @Override
  public void saveAddress(Address address) {
    addressRepository.save(address);
  }
}