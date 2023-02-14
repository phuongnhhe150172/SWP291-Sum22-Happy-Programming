package swp.happyprogramming.application.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import swp.happyprogramming.application.port.out.IConnectRepository;
import swp.happyprogramming.application.port.out.IRequestRepository;
import swp.happyprogramming.application.port.usecase.IRequestService;
import swp.happyprogramming.domain.model.Connect;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.domain.model.Request;
import swp.happyprogramming.utility.Utility;

@Service
public class RequestService implements IRequestService {

  @Autowired
  private IRequestRepository requestRepository;

  @Autowired
  private IConnectRepository connectRepository;

  @Override
  public Pagination<Request> getRequestSent(long menteeId, int pageNumber) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
    Page<Request> page = requestRepository.findRequestByMenteeId(pageRequest,
      menteeId);
    return Utility.getPagination(page);
  }

  @Override
  public Pagination<Request> getRequestReceived(long mentorId, int pageNumber) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
    Page<Request> page = requestRepository.findRequestByMentorId(pageRequest,
      mentorId);
    return Utility.getPagination(page);
  }

  @Override
  public List<Request> getRequestSent(long menteeId) {
    return requestRepository.findRequestByMenteeId(menteeId);
  }

  @Override
  public List<Request> getRequestReceived(long mentorId) {
    return requestRepository.findRequestByMentorId(mentorId);
  }

  @Override
  public Pagination<Request> getAllRequest(int pageNumber) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
    Page<Request> page = requestRepository.findAll(pageRequest);
    return Utility.getPagination(page);
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
    return requestRepository.findById(requestId).get();
  }

  @Override
  public Request findStatusRequest(long mentorId, long menteeId) {
    return requestRepository.findRequestByMentorIdAndMenteeId(mentorId,
      menteeId).orElse(null);
  }

  @Override
  public void insertRequeset(long fromId, long toId) {
    requestRepository.insertByMentorIdAndMenteeId(fromId, toId);
  }

  @Override
  public List<Long> getRequestedMentorId(long menteeId) {
    List<Request> requests = requestRepository.findRequestByMenteeId(menteeId);
    List<Long> requestedMentorId = new ArrayList<>();
    for (Request r : requests) {
      requestedMentorId.add(r.getMentor().getId());
    }
    return requestedMentorId;
  }

  public void acceptReceivedRequest(long requestId) {
    if (requestRepository.findById(requestId).isEmpty()) {
      return;
    }
    Connect connect = new Connect();
    connect.setUser1(requestRepository.findById(requestId).get().getMentor());
    connect.setUser2(requestRepository.findById(requestId).get().getMentee());
    connect.setCreated(Instant.now());
    connectRepository.save(connect);
    requestRepository.deleteById(requestId);
  }
}
