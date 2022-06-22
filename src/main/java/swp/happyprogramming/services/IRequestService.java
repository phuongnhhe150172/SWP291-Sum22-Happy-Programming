package swp.happyprogramming.services;

import swp.happyprogramming.dto.RequestDTO;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Request;

import java.util.List;

public interface IRequestService {
    Pagination<RequestDTO> getRequestSent(long menteeId, int pageNumber);
    List<RequestDTO> getRequestSent(long menteeId);
    RequestDTO convertToDto(Request request);
    Pagination<RequestDTO> getAllRequest(int pageNumber);
    long countTotalRequest();
    void deleteRequest(long requestId);
    RequestDTO getRequestById(long requestId);
    Request findStatusRequest(long mentorId, long menteeId);
}
