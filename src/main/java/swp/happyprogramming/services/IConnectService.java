package swp.happyprogramming.services;

import swp.happyprogramming.dto.ConnectDTO;
import swp.happyprogramming.model.Connect;
import swp.happyprogramming.model.User;

import java.util.List;

public interface IConnectService {
    Connect findConnectByUser1AndUser2(long user1Id, long user2Id);
    List<ConnectDTO> findAllConnections();
}
