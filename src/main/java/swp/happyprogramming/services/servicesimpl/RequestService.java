package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.RequestDTO;
import swp.happyprogramming.model.Request;
import swp.happyprogramming.model.Skill;
import swp.happyprogramming.model.User;
import swp.happyprogramming.repository.IRequestRepository;
import swp.happyprogramming.repository.ISkillRepository;
import swp.happyprogramming.repository.IUserRepository;
import swp.happyprogramming.services.IRequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService implements IRequestService {
    @Autowired
    private IRequestRepository requestRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ISkillRepository skillRepository;

    @Override
    public List<RequestDTO> getRequestSent(long menteeId) {
        List<Request> requests = new ArrayList<>();
        for (Request r: requestRepository.findAll()) {
            if (r.getMenteeId() == menteeId) requests.add(r);
        }
        return requests
                .stream()
                .map(request -> convertToDto(request))
                .collect(Collectors.toList());
    }

    @Override
    public RequestDTO convertToDto(Request request) {
        RequestDTO requestDTO = new RequestDTO();
        ModelMapper mapper = new ModelMapper();
        mapper.map(request, requestDTO);
        //get mentor, mentee fullname
        User mentor = userRepository.getById(request.getMentorId());
        requestDTO.setMentorName(mentor.getFirstName() + " " + mentor.getLastName());
        User mentee = userRepository.getById(request.getMenteeId());
        requestDTO.setMenteeName(mentee.getFirstName() + " " + mentee.getLastName());
        //get skill name
        Skill skill = skillRepository.getById(request.getSkillId());
        requestDTO.setSkillName(skill.getName());
        return requestDTO;
    }

    @Override
    public List<RequestDTO> getAllRequest() {
        return requestRepository
                .findAll()
                .stream()
                .map(request -> convertToDto(request))
                .collect(Collectors.toList());
    }
}
