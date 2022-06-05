package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.model.District;
import swp.happyprogramming.model.Province;
import swp.happyprogramming.model.Ward;
import swp.happyprogramming.repository.IAddressRepository;
import swp.happyprogramming.model.Address;
import swp.happyprogramming.repository.IDistrictRepository;
import swp.happyprogramming.repository.IProvinceRepository;
import swp.happyprogramming.repository.IWardRepository;
import swp.happyprogramming.services.IAddressService;

import java.util.Optional;

@Service
public class AddressService implements IAddressService {
    @Autowired
    private IAddressRepository addressRepository;
    @Autowired
    private IWardRepository wardRepository;
    @Autowired
    private IDistrictRepository districtRepository;
    @Autowired
    private IProvinceRepository provinceRepository;

    public String getAddress(long addressId) {
        Address address = addressRepository.findByAddressId(addressId);
        Ward ward = wardRepository.findWardById(address.getWardID());
        District district = districtRepository.findDistrictById(ward.getDistrictId());
        Province province = provinceRepository.findProvinceById(district.getProvinceId());
        return address.getName() + ", " + ward.getName() + ", " + district.getName() + ", " + province.getName();
    }
}
