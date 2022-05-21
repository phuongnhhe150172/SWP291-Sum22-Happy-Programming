package swp.happyprogramming.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Ward;

import java.util.List;

@Repository
public interface IWardRepository extends JpaRepository<Ward,Long> {
    public List<Ward> findAllByDistrictId(Long districtId);
}
