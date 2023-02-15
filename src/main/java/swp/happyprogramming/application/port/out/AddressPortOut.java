package swp.happyprogramming.application.port.out;

import swp.happyprogramming.domain.model.Address;

public interface AddressPortOut {

  Address findByAddressId(long addressId);

  String findDistrictByWardID(long wardID);

  String findProvinceByWardID(long wardID);

  void deleteById(long addressId);

  Address save(Address address);
}