package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.dto.RequestDTO;
import swp.happyprogramming.model.Pagination;
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
import java.util.stream.IntStream;

@Service
public class RequestService implements IRequestService {
    @Autowired
    private IRequestRepository requestRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ISkillRepository skillRepository;

    @Override
    public Pagination<RequestDTO> getRequestSent(long menteeId, int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        Page<Request> page = requestRepository.findRequestByMenteeId(pageRequest, menteeId);
        int totalPages = page.getTotalPages();
        List<Request> requests = page.getContent();
        List<RequestDTO> requestDTOS = requests
                .stream()
                .map(request -> convertToDto(request))
                .collect(Collectors.toList());
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        return new Pagination<>(requestDTOS, pageNumbers);
    }

    @Override
    public List<RequestDTO> getRequestSent(long menteeId) {
        List<Request> requests = requestRepository.findRequestByMenteeId(menteeId);
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
    public Pagination<RequestDTO> getAllRequest(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        Page<Request> page = requestRepository.findAll(pageRequest);
        int totalPages = page.getTotalPages();
        List<Request> requests = page.getContent();
        List<RequestDTO> requestDTOS = requests
                .stream()
                .map(request -> convertToDto(request))
                .collect(Collectors.toList());
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        return new Pagination<>(requestDTOS, pageNumbers);
    }

    @Override
    public long countTotalRequest() {
        return requestRepository.count();
    }

    @Override
    public void deleteRequest(long requestId) {
        requestRepository.deleteById(requestId);
    }

    @Override
    public RequestDTO getRequestById(long requestId) {
        Request request = requestRepository.findById(requestId).get();
        return convertToDto(request);
    }

    @Override
    public Integer findStatusRequest(long mentorId, long menteeId){
        Request request = requestRepository.findRequestByMentorIdAndMenteeId(mentorId, menteeId).orElse(null);
        if(request == null){
            return -1;
        }
        return request.getStatus();
    }
}
