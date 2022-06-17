package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.model.Request;
import swp.happyprogramming.repository.IRequestRepository;
import swp.happyprogramming.services.IRequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService implements IRequestService {
    @Autowired
    private IRequestRepository requestRepository;


    @Override
    public List<Request> getRequestSent(long menteeId) {
        List<Request> listAll = requestRepository.findAll();
        return listAll.stream()
                .filter(request -> request.getMenteeId() == menteeId)
                .collect(Collectors.toList());
    }
}
