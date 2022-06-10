package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.model.Request;
import swp.happyprogramming.repository.IRequestRepository;
import swp.happyprogramming.services.IRequestService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestService implements IRequestService {
    @Autowired
    private IRequestRepository requestRepository;


    @Override
    public List<Request> getRequestSent(long menteeId) {
        List<Request> listAll = requestRepository.findAll();
        List<Request> res = new ArrayList<>();
        for (Request r: listAll) {
            if (r.getMenteeId() == menteeId) res.add(r);
        }
        return res;
    }
}
