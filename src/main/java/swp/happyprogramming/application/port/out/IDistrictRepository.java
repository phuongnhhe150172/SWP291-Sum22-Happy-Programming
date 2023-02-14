package swp.happyprogramming.application.port.out;

import java.util.List;
import java.util.Optional;
import swp.happyprogramming.domain.model.District;


public interface IDistrictRepository {

  List<District> findAllByProvinceId(long provinceId);

  District findDistrictById(long districtId);

  Optional<District> findById(long districtId);
}
