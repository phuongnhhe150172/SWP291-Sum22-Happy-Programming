package swp.happyprogramming.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swp.happyprogramming.model.Address;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO extends Address {
//    private long id;
    private String name;
    private WardDTO ward;
    private DistrictDTO district;
    private ProvinceDTO province;

    @Override
    public String toString() {
        return name + ", "
                + ward.getName() + ", "
                + district.getName() + ", "
                + province.getName();
    }
}