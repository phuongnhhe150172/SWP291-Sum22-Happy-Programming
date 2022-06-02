package swp.happyprogramming.services;

import swp.happyprogramming.dto.ProvinceDTO;

import java.util.List;

public interface IProvinceService {
    List<ProvinceDTO> findAllProvinces();

    long getProvinceIdByDistrictId(long districtId);
}
