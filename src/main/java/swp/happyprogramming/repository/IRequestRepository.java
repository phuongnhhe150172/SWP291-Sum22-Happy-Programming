package swp.happyprogramming.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Request;

import java.util.List;

@Repository
public interface IRequestRepository extends JpaRepository<Request, Long> {
    Page<Request> findRequestByMenteeId(Pageable pageable, long menteeId);
    List<Request> findRequestByMenteeId(long menteeId);
}
