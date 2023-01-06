package swp.happyprogramming.application.usecase;

import swp.happyprogramming.domain.dto.ConnectDTO;
import swp.happyprogramming.domain.model.Connect;
import swp.happyprogramming.domain.model.Pagination;

import java.util.List;

public interface IConnectService {
    Connect findConnectByUser1AndUser2(long user1Id, long user2Id);
    List<ConnectDTO> findAllConnections();
    Pagination<ConnectDTO> findAllConnections(int pageNumber);
    List<Long> getConnectedMentor(long menteeId);
    void disconnect(long user1, long user2);
}
