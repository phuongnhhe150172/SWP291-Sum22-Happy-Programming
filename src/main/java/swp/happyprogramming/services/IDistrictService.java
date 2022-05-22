package swp.happyprogramming.services;

import swp.happyprogramming.dto.DistrictDTO;

import java.util.List;

public interface IDistrictService {
    List<DistrictDTO> findAllDistrict(long id);
}
