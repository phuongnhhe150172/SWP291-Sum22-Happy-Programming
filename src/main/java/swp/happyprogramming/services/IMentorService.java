package swp.happyprogramming.services;

import swp.happyprogramming.dto.MentorDTO;

public interface IMentorService {
    MentorDTO findMentor(long id);
}
