package swp.happyprogramming.services;

import swp.happyprogramming.model.Request;

import java.util.List;

public interface IRequestService {
    List<Request> getRequestSent(long menteeId);
}
