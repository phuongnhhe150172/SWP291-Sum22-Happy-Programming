package swp.happyprogramming.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private  long id;
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