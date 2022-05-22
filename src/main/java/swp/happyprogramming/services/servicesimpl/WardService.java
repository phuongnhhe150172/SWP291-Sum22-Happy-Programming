package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dao.IAddressRepository;
import swp.happyprogramming.dao.IWardRepository;
import swp.happyprogramming.dto.WardDTO;
import swp.happyprogramming.model.Address;
import swp.happyprogramming.model.Ward;
import swp.happyprogramming.services.IWardService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WardService implements IWardService {
    @Autowired
    private IWardRepository wardRepository;

    @Autowired
    private IAddressRepository addressRepository;

    public List<WardDTO> findAllWard(long districtId){
        ModelMapper mapper = new ModelMapper();
        List<Ward> list = wardRepository.findAllByDistrictId(districtId);
        return list.stream().map(value -> mapper.map(value,WardDTO.class)).collect(Collectors.toList());
    }

    public long getWardIdByProfileId(long profileId){
        Address address = addressRepository.findByProfileID(profileId).orElse(null);
        return address.getWardID();
    }
}
