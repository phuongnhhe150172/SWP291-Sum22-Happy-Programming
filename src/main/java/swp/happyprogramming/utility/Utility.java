package swp.happyprogramming.utility;

import org.modelmapper.ModelMapper;
import swp.happyprogramming.dto.*;
import swp.happyprogramming.model.Address;
import swp.happyprogramming.model.Feedback;
import swp.happyprogramming.model.User;
import swp.happyprogramming.model.Ward;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class Utility {

    private static final ModelMapper mapper = new ModelMapper();

    private Utility() {
        //    Overwrite default constructor
    }

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public static UserDTO mapUser(User user) {
        if (user == null) return null;
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setAddress(mapAddress(user.getAddress()));
        return userDTO;
    }

    public static AddressDTO mapAddress(Address address) {
        AddressDTO addressDTO = mapper.map(address, AddressDTO.class);
        addressDTO.setWard(mapper.map(address.getWard(), WardDTO.class));
        addressDTO.setDistrict(mapper.map(address.getWard().getDistrict(), DistrictDTO.class));
        addressDTO.setProvince(mapper.map(address.getWard().getDistrict().getProvince(), ProvinceDTO.class));
        return addressDTO;
    }

    public static Address mapAddressDTO(AddressDTO addressDTO, long wardId) {
        Address address = mapper.map(addressDTO, Address.class);
        Ward ward = new Ward();
        ward.setId(wardId);
        address.setWard(ward);
        return address;
    }

    public static double getAverageRate(List<Feedback> feedback) {
        if (feedback.isEmpty()) return 0;
        return (double) feedback.stream().mapToInt(Feedback::getRate).sum() / feedback.size();
    }

}
