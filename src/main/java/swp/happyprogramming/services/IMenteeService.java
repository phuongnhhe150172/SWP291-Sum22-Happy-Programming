package swp.happyprogramming.services;

import swp.happyprogramming.dto.MenteeDTO;
import swp.happyprogramming.dto.MentorDTO;

public interface IMenteeService {
    MenteeDTO findMentee(long id);
}
