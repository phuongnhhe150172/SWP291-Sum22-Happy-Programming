package swp.happyprogramming.application.usecase;

import swp.happyprogramming.domain.dto.ProvinceDTO;

import java.util.List;

public interface IProvinceService {
    List<ProvinceDTO> findAllProvinces();

    long getProvinceIdByDistrictId(long districtId);
}
