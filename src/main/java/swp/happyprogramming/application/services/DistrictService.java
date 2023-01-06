package swp.happyprogramming.application.services;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.adapter.port.out.IDistrictRepository;
import swp.happyprogramming.adapter.port.out.IWardRepository;
import swp.happyprogramming.application.usecase.IDistrictService;
import swp.happyprogramming.domain.dto.DistrictDTO;
import swp.happyprogramming.domain.model.District;
import swp.happyprogramming.domain.model.Ward;

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