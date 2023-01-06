package swp.happyprogramming.adapter.port.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.domain.model.Ward;

import java.util.List;

@Repository
public interface IWardRepository extends JpaRepository<Ward, Long> {
    public List<Ward> findAllByDistrictId(long districtId);

    Ward findWardById(long wardID);

    @Query(value = "SELECT * FROM ward LIMIT 1", nativeQuery = true)
    Ward findFirst();
}
