package swp.happyprogramming.application.usecase;

import swp.happyprogramming.adapter.dto.ProvinceDTO;

import java.util.List;

public interface IProvinceService {
    List<ProvinceDTO> findAllProvinces();

    long getProvinceIdByDistrictId(long districtId);
}
