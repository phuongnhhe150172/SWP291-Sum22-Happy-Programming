package swp.happyprogramming.utility;

import org.modelmapper.ModelMapper;
import swp.happyprogramming.dto.*;
import swp.happyprogramming.model.Address;
import swp.happyprogramming.model.User;
import swp.happyprogramming.model.Ward;

import javax.servlet.http.HttpServletRequest;

public class Utility {

    private Utility() {

    }

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public static UserDTO mapUser(User user) {
        ModelMapper mapper = new ModelMapper();
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setAddress(mapAddress(user.getAddress()));
        return userDTO;
    }

    public static AddressDTO mapAddress(Address address) {
        ModelMapper mapper = new ModelMapper();
        AddressDTO addressDTO = mapper.map(address, AddressDTO.class);
        addressDTO.setWard(mapper.map(address.getWard(), WardDTO.class));
        addressDTO.setDistrict(mapper.map(address.getWard().getDistrict(), DistrictDTO.class));
        addressDTO.setProvince(mapper.map(address.getWard().getDistrict().getProvince(), ProvinceDTO.class));
        return addressDTO;
    }

    public static Address mapAddressDTO(AddressDTO addressDTO) {
        ModelMapper mapper = new ModelMapper();
        Address address = mapper.map(addressDTO, Address.class);
        address.setWard(mapper.map(addressDTO.getWard(), Ward.class));
        return address;
    }

}
