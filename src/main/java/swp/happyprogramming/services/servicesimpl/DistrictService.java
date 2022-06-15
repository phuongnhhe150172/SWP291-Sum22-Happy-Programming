package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.repository.IDistrictRepository;
import swp.happyprogramming.repository.IWardRepository;
import swp.happyprogramming.dto.DistrictDTO;
import swp.happyprogramming.model.District;
import swp.happyprogramming.model.Ward;
import swp.happyprogramming.services.IDistrictService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistrictService implements IDistrictService {

    @Autowired
    private IDistrictRepository districtRepository;

    @Autowired
    private IWardRepository wardRepository;

    public List<DistrictDTO> findAllDistrict(long provinceId){
        ModelMapper mapper = new ModelMapper();
        List<District> list = districtRepository.findAllByProvinceId(provinceId);
        return list.stream().map(value -> mapper.map(value,DistrictDTO.class)).collect(Collectors.toList());
    }

    public long getDistrictIdByWardId(long wardId){
        Ward ward = wardRepository.findById(wardId).orElse(new Ward());
        return ward.getDistrict().getId();
    }
}