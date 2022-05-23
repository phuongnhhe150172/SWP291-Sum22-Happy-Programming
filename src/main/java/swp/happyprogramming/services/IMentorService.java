package swp.happyprogramming.services;

import swp.happyprogramming.dto.MentorDTO;

public interface IMentorService {
    MentorDTO findMentor(long id);

    void updateMentor(long mentorId, MentorDTO mentor, long wardId);
}
