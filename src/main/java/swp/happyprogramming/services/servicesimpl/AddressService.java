package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import swp.happyprogramming.dao.IAddressRepository;
import swp.happyprogramming.model.Address;
import swp.happyprogramming.services.IAddressService;

public class AddressService implements IAddressService {
    @Autowired
    private IAddressRepository addressRepository;

    public String getAddress(int profileID) {
        Address address = addressRepository.findAddressById(profileID);
        if (address == null) {
            return "";
        } else {
            return address.getName();
        }
    }
}