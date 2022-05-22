package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.repository.IWardRepository;
import swp.happyprogramming.dto.WardDTO;
import swp.happyprogramming.model.Ward;
import swp.happyprogramming.services.IWardService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WardService implements IWardService {
    @Autowired
    private IWardRepository wardRepository;

    public List<WardDTO> findAllWard(long districtId){
        ModelMapper mapper = new ModelMapper();
        List<Ward> list = wardRepository.findAllByDistrictId(districtId);
        return list.stream().map(value -> mapper.map(value,WardDTO.class)).collect(Collectors.toList());
    }
}
