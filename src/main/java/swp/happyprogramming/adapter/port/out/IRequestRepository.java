package swp.happyprogramming.adapter.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.domain.model.Request;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface IRequestRepository extends JpaRepository<Request, Long> {
    Page<Request> findRequestByMenteeId(Pageable pageable, long menteeId);

    List<Request> findRequestByMenteeId(long id);

    Page<Request> findRequestByMentorId(Pageable pageable, long mentorId);

    List<Request> findRequestByMentorId(long id);

    Optional<Request> findRequestByMentorIdAndMenteeId(long mentorId, long menteeId);

    @Modifying
    @Transactional
    @Query(value = "Insert into request(mentor_id,mentee_id) values (?1,?2)", nativeQuery = true)
    void insertByMentorIdAndMenteeId(long mentorId, long menteeId);
}
