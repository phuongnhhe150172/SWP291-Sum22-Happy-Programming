package swp.happyprogramming.application.usecase;

import swp.happyprogramming.domain.dto.DistrictDTO;

import java.util.List;

public interface IDistrictService {
    List<DistrictDTO> findAllDistrict(long id);

    long getDistrictIdByWardId(long wardId);
}
