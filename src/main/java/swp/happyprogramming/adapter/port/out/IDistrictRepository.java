package swp.happyprogramming.adapter.port.out;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.application.port.out.DistrictPortOut;
import swp.happyprogramming.domain.model.District;

@Repository("districtRepository")
public interface IDistrictRepository extends JpaRepository<District, Long> ,
  DistrictPortOut {

  List<District> findAllByProvinceId(long provinceId);

  District findDistrictById(long districtId);
}
