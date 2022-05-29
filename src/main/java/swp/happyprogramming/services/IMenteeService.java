package swp.happyprogramming.services;

import swp.happyprogramming.dto.MenteeDTO;

import java.util.List;

public interface IMenteeService {
    MenteeDTO findMentee(long id);

    List<MenteeDTO> getAllMentees();
}
