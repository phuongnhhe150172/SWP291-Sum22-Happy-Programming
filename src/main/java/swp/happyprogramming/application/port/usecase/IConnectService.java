package swp.happyprogramming.application.port.usecase;

import java.util.List;
import swp.happyprogramming.application.dto.ConnectDTO;
import swp.happyprogramming.domain.model.Connect;
import swp.happyprogramming.domain.model.Pagination;

public interface IConnectService {

  Connect findConnectByUser1AndUser2(long user1Id, long user2Id);

  List<ConnectDTO> findAllConnections();

  Pagination<ConnectDTO> findAllConnections(int pageNumber);

  List<Long> getConnectedMentor(long menteeId);

  void disconnect(long user1, long user2);
}
