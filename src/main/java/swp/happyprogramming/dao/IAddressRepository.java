package swp.happyprogramming.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swp.happyprogramming.model.Address;

public interface IAddressRepository extends JpaRepository<Address, Long> {
    Address findAddressById(long id);

    @Query(value = "SELECT NAME FROM DISTRICT WHERE ID IN (SELECT DISTRICT_ID FROM WARD WHERE ID=?1)",
            nativeQuery = true)
    String findDistrictByWardID(long wardID);

    @Query(value = "SELECT NAME FROM DISTRICT WHERE ID IN (SELECT DISTRICT_ID FROM WARD WHERE ID=?1)",
            nativeQuery = true)
    String findProvinceByWardID(long wardID);
}
