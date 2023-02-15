package swp.happyprogramming.adapter.port.out;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.application.port.out.WardPortOut;
import swp.happyprogramming.domain.model.Ward;

@Repository("wardRepository")
public interface IWardRepository extends JpaRepository<Ward, Long> ,
  WardPortOut {

  List<Ward> findAllByDistrictId(long districtId);

  Ward findWardById(long wardID);

  @Query(value = "SELECT * FROM ward LIMIT 1", nativeQuery = true)
  Ward findFirst();
}
