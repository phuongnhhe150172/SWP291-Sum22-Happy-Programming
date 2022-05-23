package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import swp.happyprogramming.repository.IAddressRepository;
import swp.happyprogramming.model.Address;
import swp.happyprogramming.services.IAddressService;

import java.util.Optional;

public class AddressService implements IAddressService {
    @Autowired
    private IAddressRepository addressRepository;

    public String getAddress(int profileID) {
        Address address = addressRepository.findByProfileID(profileID).orElse(null);
        if (address == null) {
            return "";
        } else {
            return address.getName();
        }
    }
}
