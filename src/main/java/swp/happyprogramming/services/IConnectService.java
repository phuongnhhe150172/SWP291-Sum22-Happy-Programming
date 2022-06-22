package swp.happyprogramming.services;

import swp.happyprogramming.model.Connect;
import swp.happyprogramming.model.User;

public interface IConnectService {
    Connect findConnectByUser1AndUser2(long user1Id, long user2Id);
}
