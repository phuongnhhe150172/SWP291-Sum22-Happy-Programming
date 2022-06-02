package swp.happyprogramming.services;

import swp.happyprogramming.dto.WardDTO;

import java.util.List;

public interface IWardService {
    List<WardDTO> findAllWard(long id);

    long getWardIdByProfileId(long profileId);
}
