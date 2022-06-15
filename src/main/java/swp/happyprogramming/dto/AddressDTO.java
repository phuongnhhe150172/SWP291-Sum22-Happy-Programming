package swp.happyprogramming.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String addressName;
    private WardDTO ward;
    private DistrictDTO district;
    private ProvinceDTO province;

    @Override
    public String toString() {
        return addressName + ", "
                + ward.getName() + ", "
                + district.getName() + ", "
                + province.getName();
    }
}