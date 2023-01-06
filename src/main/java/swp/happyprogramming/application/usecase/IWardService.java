package swp.happyprogramming.application.usecase;

import swp.happyprogramming.domain.dto.WardDTO;

import java.util.List;

public interface IWardService {
    List<WardDTO> findAllWard(long id);

    long getWardIdByAddressId(long addressId);
}
