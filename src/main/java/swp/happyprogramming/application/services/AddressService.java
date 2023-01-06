package swp.happyprogramming.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.adapter.port.out.IAddressRepository;
import swp.happyprogramming.adapter.port.out.IDistrictRepository;
import swp.happyprogramming.adapter.port.out.IProvinceRepository;
import swp.happyprogramming.adapter.port.out.IWardRepository;
import swp.happyprogramming.application.usecase.IAddressService;
import swp.happyprogramming.domain.model.Address;
import swp.happyprogramming.domain.model.District;
import swp.happyprogramming.domain.model.Province;
import swp.happyprogramming.domain.model.Ward;

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

    @Override
    public String getAddress(long addressId) {
        Address address = addressRepository.findByAddressId(addressId);
        Ward ward = wardRepository.findWardById(address.getWard().getId());
        District district = districtRepository.findDistrictById(ward.getDistrict().getId());
        Province province = provinceRepository.findProvinceById(district.getProvince().getId());
        return address.getName() + ", " + ward.getName() + ", " + district.getName() + ", " + province.getName();
    }
}