package swp.happyprogramming.application.port.out;

import java.util.List;
import swp.happyprogramming.domain.model.Province;

public interface IProvinceRepository {

  Province findProvinceById(long provinceId);

  List<Province> findAll();
}