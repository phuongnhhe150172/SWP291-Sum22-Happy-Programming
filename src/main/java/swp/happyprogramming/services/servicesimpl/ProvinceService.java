package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.repository.IProvinceRepository;
import swp.happyprogramming.dto.ProvinceDTO;
import swp.happyprogramming.model.Province;
import swp.happyprogramming.services.IProvinceService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProvinceService implements IProvinceService {
    @Autowired
    private IProvinceRepository provinceRepository;

    public List<ProvinceDTO> findAllProvinces(){
        ModelMapper mapper = new ModelMapper();
        List<Province> list = provinceRepository.findAll();
        return list.stream().map(value -> mapper.map(value, ProvinceDTO.class)).collect(Collectors.toList());
    }
}