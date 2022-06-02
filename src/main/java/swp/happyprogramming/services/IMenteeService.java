package swp.happyprogramming.services;

import swp.happyprogramming.dto.MenteeDTO;
import swp.happyprogramming.dto.MentorDTO;

import java.util.List;

public interface IMenteeService {
    MenteeDTO findMentee(long id);

    List<MenteeDTO> getAllMentees();

    void updateMentee(long menteeId, MenteeDTO mentee, long wardId, long wa);

}
