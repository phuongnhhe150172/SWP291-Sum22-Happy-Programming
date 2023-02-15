package swp.happyprogramming.adapter.port.out;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.application.port.out.AddressPortOut;
import swp.happyprogramming.domain.model.Address;

@Repository("addressRepository")
public interface IAddressRepository extends JpaRepository<Address, Long>,
  AddressPortOut {

  @Query(value = "select * from address as a where a.id = ?1", nativeQuery = true)
  Address findByAddressId(long addressId);

  @Query(value = "SELECT NAME FROM DISTRICT WHERE ID IN (SELECT DISTRICT_ID FROM WARD WHERE ID=?1)",
    nativeQuery = true)
  String findDistrictByWardID(long wardID);

  @Query(value = "SELECT NAME FROM DISTRICT WHERE ID IN (SELECT DISTRICT_ID FROM WARD WHERE ID=?1)",
    nativeQuery = true)
  String findProvinceByWardID(long wardID);

  List<Address> findAll();
}