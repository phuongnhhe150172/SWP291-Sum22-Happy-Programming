package swp.happyprogramming.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Address;

import java.util.Optional;

@Repository
public interface IAddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByProfileID(long id);

    @Query(value = "select a.id,a.`name`,a.ward_id,a.profile_id from address as a where a.profile_id=?1 and a.ward_id=?2",nativeQuery = true)
    Address findByProfileIDAndWardID(long profileId,long wardId);

    @Query(value = "SELECT NAME FROM DISTRICT WHERE ID IN (SELECT DISTRICT_ID FROM WARD WHERE ID=?1)",
            nativeQuery = true)
    String findDistrictByWardID(long wardID);

    @Query(value = "SELECT NAME FROM DISTRICT WHERE ID IN (SELECT DISTRICT_ID FROM WARD WHERE ID=?1)",
            nativeQuery = true)
    String findProvinceByWardID(long wardID);
}
