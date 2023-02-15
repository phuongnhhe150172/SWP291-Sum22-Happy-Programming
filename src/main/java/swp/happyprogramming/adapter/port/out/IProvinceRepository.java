package swp.happyprogramming.adapter.port.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.application.port.out.ProvincePortOut;
import swp.happyprogramming.domain.model.Province;

@Repository("provinceRepository")
public interface IProvinceRepository extends JpaRepository<Province, Long> ,
  ProvincePortOut {

  Province findProvinceById(long provinceId);
}
