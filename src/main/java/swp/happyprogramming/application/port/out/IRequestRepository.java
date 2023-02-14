package swp.happyprogramming.application.port.out;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import swp.happyprogramming.domain.model.Request;

public interface IRequestRepository {

  Page<Request> findRequestByMenteeId(Pageable pageable, long menteeId);

  List<Request> findRequestByMenteeId(long id);

  Page<Request> findRequestByMentorId(Pageable pageable, long mentorId);

  List<Request> findRequestByMentorId(long id);

  Optional<Request> findRequestByMentorIdAndMenteeId(long mentorId,
    long menteeId);

  void insertByMentorIdAndMenteeId(long mentorId, long menteeId);

  Page<Request> findAll(PageRequest pageRequest);

  long count();

  void deleteById(long requestId);

  Optional<Request> findById(long requestId);
}