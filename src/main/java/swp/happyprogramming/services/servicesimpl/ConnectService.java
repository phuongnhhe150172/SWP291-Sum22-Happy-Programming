package swp.happyprogramming.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.model.Connect;
import swp.happyprogramming.model.User;
import swp.happyprogramming.repository.IConnectRepository;
import swp.happyprogramming.services.IConnectService;

@Service
public class ConnectService implements IConnectService {
    @Autowired
    private IConnectRepository connectRepository;

    @Override
    public Connect findConnectByUser1AndUser2(long user1Id, long user2Id){
        return connectRepository.findConnectByUser1IdAndUser2Id(user1Id, user2Id).orElse(null);
    }
}
