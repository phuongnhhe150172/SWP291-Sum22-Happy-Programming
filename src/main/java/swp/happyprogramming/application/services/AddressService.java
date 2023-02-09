package swp.happyprogramming.application.services;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.adapter.dto.DistrictDTO;
import swp.happyprogramming.adapter.dto.MentorDTO;
import swp.happyprogramming.adapter.dto.ProvinceDTO;
import swp.happyprogramming.adapter.dto.WardDTO;
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
    ModelMapper mapper = new ModelMapper();
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

    @Override
    public List<WardDTO> findAllWard(long districtId) {
        ModelMapper mapper = new ModelMapper();
        List<Ward> list = wardRepository.findAllByDistrictId(districtId);
        return list.stream().map(value -> mapper.map(value, WardDTO.class)).collect(Collectors.toList());
    }

    @Override
    public long getWardIdByAddressId(long addressId) {
        Address address = addressRepository.findByAddressId(addressId);
        return address.getWard().getId();
    }

    @Override
    public List<DistrictDTO> findAllDistrict(long provinceId){
        ModelMapper mapper = new ModelMapper();
        List<District> list = districtRepository.findAllByProvinceId(provinceId);
        return list.stream().map(value -> mapper.map(value,DistrictDTO.class)).collect(Collectors.toList());
    }

    @Override
    public long getDistrictIdByWardId(long wardId){
        Ward ward = wardRepository.findById(wardId).orElse(new Ward());
        return ward.getDistrict().getId();
    }

    @Override
    public List<ProvinceDTO> findAllProvinces() {
        ModelMapper mapper = new ModelMapper();
        List<Province> list = provinceRepository.findAll();
        return list.stream().map(value -> mapper.map(value, ProvinceDTO.class)).collect(Collectors.toList());
    }

    @Override
    public long getProvinceIdByDistrictId(long districtId) {
        District district = districtRepository.findById(districtId).orElse(new District());
        return district.getProvince().getId();
    }
    public void updateAddress(MentorDTO mentorDTO, long wardId) {
        Address address = mapper.map(mentorDTO.getAddress(), Address.class);
        Ward ward = wardRepository.findById(wardId).orElse(new Ward());
        address.setWard(ward);
        address.setName(mentorDTO.getAddress().getName());
        addressRepository.save(address);
    }
}