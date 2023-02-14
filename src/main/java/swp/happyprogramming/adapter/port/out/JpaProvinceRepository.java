package swp.happyprogramming.adapter.port.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.domain.model.Province;

@Repository
public interface JpaProvinceRepository extends JpaRepository<Province, Long> {

  Province findProvinceById(long provinceId);
}
