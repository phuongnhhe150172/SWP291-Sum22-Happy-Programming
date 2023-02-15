package swp.happyprogramming.application.port.out;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import swp.happyprogramming.domain.model.User;

public interface UserPortOut {

  User findByEmail(String email);

  int countUsersByRolesLike(String role);

  Page<User> findAll(Specification<User> specification, Pageable pageable);

  Optional<Integer> statusRequestByMentorIdAndMenteeId(long mentorId,
    long menteeId);

  Page<User> findConnectionsById(Pageable pageable, long id);

  List<User> findConnectionsById(long id);

  ArrayList<User> findRequestsByEmail(String email);

  User findByResetPasswordToken(String token);

  void enableUser(long id);

  void disableUser(long id);

  void convertToMentor(long userId, long roleId);

  List<Integer> getListAmountNewMentees();

  User save(User user);

  Optional<User> findById(Long id);

  void deleteById(long menteeId);
}