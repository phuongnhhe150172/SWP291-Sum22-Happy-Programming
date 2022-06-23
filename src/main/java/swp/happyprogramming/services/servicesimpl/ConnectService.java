package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.ConnectDTO;
import swp.happyprogramming.model.Connect;
import swp.happyprogramming.repository.IConnectRepository;
import swp.happyprogramming.services.IConnectService;
import swp.happyprogramming.utility.Utility;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectService implements IConnectService {
    @Autowired
    private IConnectRepository connectRepository;

    ModelMapper mapper = new ModelMapper();

    @Override
    public Connect findConnectByUser1AndUser2(long user1Id, long user2Id){
        return connectRepository.findConnectByUser1IdAndUser2Id(user1Id, user2Id).orElse(null);
    }

    @Override
    public List<ConnectDTO> findAllConnections() {
        List<Connect> connects = connectRepository.findAll();
        List<ConnectDTO> connectDTOS = connects.stream().map(connect -> mapper.map(connect, ConnectDTO.class)).collect(Collectors.toList());
        for (int i = 0; i < connects.size(); i++) {
            connectDTOS.get(i).setUser1(Utility.mapUser(connects.get(i).getUser1()));
            connectDTOS.get(i).setUser2(Utility.mapUser(connects.get(i).getUser2()));
        }
        return connectDTOS;
    }
}
