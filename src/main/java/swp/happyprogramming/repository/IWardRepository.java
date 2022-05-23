package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Ward;

import java.util.List;
import java.util.Optional;

@Repository
public interface IWardRepository extends JpaRepository<Ward,Long> {
    public List<Ward> findAllByDistrictId(Long districtId);
}
