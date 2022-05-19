package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dao.IMentorRepository;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.model.Mentor;
import swp.happyprogramming.services.IMentorService;

import java.util.Optional;

@Service
public class MentorService implements IMentorService {
    @Autowired
    private IMentorRepository repository;

    public MentorDTO findMentor(long id) {
        Optional<Mentor> mentorOptional = repository.findById(id);
        if (mentorOptional.isPresent()) {
            Mentor mentor = mentorOptional.get();
            ModelMapper mapper = new ModelMapper();
            MentorDTO mentorDTO = mapper.map(mentor, MentorDTO.class);
            mentorDTO.setFullname("a");
            return mentorDTO;
        } else {
            return null;
        }
    }
}
