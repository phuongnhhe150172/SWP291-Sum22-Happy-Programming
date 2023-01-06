package swp.happyprogramming.application.usecase;

import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Request;

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


    Pagination<Request> getRequestReceived(long mentorId, int pageNumber);

    List<Request> getRequestReceived(long mentorId);

    void acceptReceivedRequest(long requestId);

    Request getRequestByMentorIdAndMenteeId(long mentorId, long menteeId);

    void deleteReceivedRequest(long requestId);

}
