package swp.happyprogramming.adapter.port.out;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.domain.model.District;

@Repository
public interface JpaDistrictRepository extends JpaRepository<District, Long> {

  List<District> findAllByProvinceId(long provinceId);

  District findDistrictById(long districtId);
}
