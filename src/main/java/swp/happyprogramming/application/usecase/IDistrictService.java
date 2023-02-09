package swp.happyprogramming.application.usecase;

import swp.happyprogramming.adapter.dto.DistrictDTO;

import java.util.List;

public interface IDistrictService {
    List<DistrictDTO> findAllDistrict(long id);

    long getDistrictIdByWardId(long wardId);
}
