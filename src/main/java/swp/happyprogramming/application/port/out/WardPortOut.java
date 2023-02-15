package swp.happyprogramming.application.port.out;

import java.util.List;
import java.util.Optional;
import swp.happyprogramming.domain.model.Ward;

public interface WardPortOut {

  List<Ward> findAllByDistrictId(long districtId);

  Ward findWardById(long wardID);

  Ward findFirst();

  Optional<Ward> findById(long wardId);
}