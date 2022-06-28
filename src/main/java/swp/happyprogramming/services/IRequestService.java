package swp.happyprogramming.services;

import swp.happyprogramming.dto.RequestDTO;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Request;
import swp.happyprogramming.model.User;

import java.util.List;

public interface IRequestService {
    Pagination<Request> getRequestSent(long menteeId, int pageNumber);
    List<Request> getRequestSent(long menteeId);
    Pagination<Request> getAllRequest(int pageNumber);
    long countTotalRequest();
    void deleteRequest(long requestId);
    Request getRequestById(long requestId);
    Request findStatusRequest(long mentorId, long menteeId);
    void insertRequeset(long fromId, long toId);
    List<Long> getRequestedMentorId(long menteeId);
}
