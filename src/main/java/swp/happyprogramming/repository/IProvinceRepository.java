package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Province;

@Repository
public interface IProvinceRepository extends JpaRepository<Province,Long> {

}
