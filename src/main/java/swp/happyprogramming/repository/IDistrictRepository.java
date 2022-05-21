package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.District;

import java.util.List;

@Repository
public interface IDistrictRepository extends JpaRepository<District,Long> {
    public List<District> findAllByProvinceId(Long provinceId);
}
