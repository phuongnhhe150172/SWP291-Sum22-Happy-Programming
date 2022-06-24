package swp.happyprogramming.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import swp.happyprogramming.model.Request;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface IRequestRepository extends JpaRepository<Request, Long> {
    Page<Request> findRequestByMenteeId(Pageable pageable, long menteeId);
    List<Request> findRequestByMenteeId(long menteeId);

    Optional<Request> findRequestByMentorIdAndMenteeId(long mentorId, long menteeId);

    @Modifying
    @Transactional
    @Query(value = "Insert into request(mentor_id,mentee_id) values (?1,?2)",nativeQuery = true)
    void insertByMentorIdAndMenteeId(long mentorId,long menteeId);
}
