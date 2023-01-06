package swp.happyprogramming.application.services;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.adapter.port.out.IAddressRepository;
import swp.happyprogramming.adapter.port.out.IWardRepository;
import swp.happyprogramming.application.usecase.IWardService;
import swp.happyprogramming.domain.dto.WardDTO;
import swp.happyprogramming.domain.model.Address;
import swp.happyprogramming.domain.model.Ward;

@Service
public class WardService implements IWardService {
    @Autowired
    private IWardRepository wardRepository;

    @Autowired
    private IAddressRepository addressRepository;

    public List<WardDTO> findAllWard(long districtId) {
        ModelMapper mapper = new ModelMapper();
        List<Ward> list = wardRepository.findAllByDistrictId(districtId);
        return list.stream().map(value -> mapper.map(value, WardDTO.class)).collect(Collectors.toList());
    }

    public long getWardIdByAddressId(long addressId) {
        Address address = addressRepository.findByAddressId(addressId);
        return address.getWard().getId();
    }
}