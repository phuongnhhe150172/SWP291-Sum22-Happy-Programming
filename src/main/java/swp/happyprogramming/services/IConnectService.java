package swp.happyprogramming.services;

import swp.happyprogramming.dto.ConnectDTO;
import swp.happyprogramming.dto.RequestDTO;
import swp.happyprogramming.model.Connect;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.User;

import java.util.List;
import java.util.Map;

public interface IConnectService {
    Connect findConnectByUser1AndUser2(long user1Id, long user2Id);
    List<ConnectDTO> findAllConnections();
    Pagination<ConnectDTO> findAllConnections(int pageNumber);
    List<Long> getConnectedMentor(long menteeId);
    void disconnect(long user1, long user2);
}
