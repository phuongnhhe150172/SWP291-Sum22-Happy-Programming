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
    public Pagination<Request> getRequestSent(long menteeId, int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        Page<Request> page = requestRepository.findRequestByMenteeId(pageRequest, menteeId);
        int totalPages = page.getTotalPages();
        List<Request> requests = page.getContent();
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        return new Pagination<>(requests, pageNumbers);
    }

    @Override
    public List<Request> getRequestSent(long menteeId) {
        List<Request> requests = requestRepository.findRequestByMenteeId(menteeId);
        return requests;
    }


    @Override
    public Pagination<Request> getAllRequest(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        Page<Request> page = requestRepository.findAll(pageRequest);
        int totalPages = page.getTotalPages();
        List<Request> requests = page.getContent();
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        return new Pagination<>(requests, pageNumbers);
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
    public Request getRequestById(long requestId) {
        Request request = requestRepository.findById(requestId).get();
        return request;
    }

    @Override
    public Request findStatusRequest(long mentorId, long menteeId){
        return requestRepository.findRequestByMentorIdAndMenteeId(mentorId, menteeId).orElse(null);
    }

    @Override
    public void insertRequeset(long fromId, long toId){
        requestRepository.insertByMentorIdAndMenteeId(fromId, toId);
    }

    @Override
    public List<Long> getRequestedMentorId(long menteeId) {
        List<Request> requests = requestRepository.findRequestByMenteeId(menteeId);
        List<Long> requestedMentorId = new ArrayList<>();
        for (Request r :
                requests) {
            requestedMentorId.add(r.getMentor().getId());
        }
        return requestedMentorId;
    }

    @Override
    public long countMonthlyRequest() {
        return 0;
    }
}
