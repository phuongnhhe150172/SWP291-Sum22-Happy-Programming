package swp.happyprogramming.services;

import swp.happyprogramming.dto.RequestDTO;
import swp.happyprogramming.model.Request;

import java.util.List;

public interface IRequestService {
    List<RequestDTO> getRequestSent(long menteeId);
    RequestDTO convertToDto(Request request);
    List<RequestDTO> getAllRequest();
}
