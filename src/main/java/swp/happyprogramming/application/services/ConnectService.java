package swp.happyprogramming.application.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import swp.happyprogramming.adapter.dto.ConnectDTO;
import swp.happyprogramming.application.port.out.IConnectRepository;
import swp.happyprogramming.application.port.usecase.IConnectService;
import swp.happyprogramming.domain.model.Connect;
import swp.happyprogramming.domain.model.Pagination;
import swp.happyprogramming.utility.Utility;

@Service
public class ConnectService implements IConnectService {

  ModelMapper mapper = new ModelMapper();
  @Autowired
  private IConnectRepository connectRepository;

  @Override
  public Connect findConnectByUser1AndUser2(long user1Id, long user2Id) {
    return connectRepository.findConnectByUser1IdAndUser2Id(user1Id, user2Id)
      .orElse(null);
  }

  @Override
  public List<ConnectDTO> findAllConnections() {
    List<Connect> connects = connectRepository.findAll();
    List<ConnectDTO> connectDTOS = connects.stream()
      .map(connect -> mapper.map(connect, ConnectDTO.class))
      .collect(Collectors.toList());
    for (int i = 0; i < connects.size(); i++) {
      connectDTOS.get(i).setCreated(Date.from(connects.get(i).getCreated()));
      connectDTOS.get(i).setUser1(Utility.mapUser(connects.get(i).getUser1()));
      connectDTOS.get(i).setUser2(Utility.mapUser(connects.get(i).getUser2()));
    }
    return connectDTOS;
  }

  @Override
  public Pagination<ConnectDTO> findAllConnections(int pageNumber) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
    Page<Connect> page = connectRepository.findAll(pageRequest);
    return Utility.getPagination(page, this::mapConnect);
  }

  //map connect to connectDTO
  private ConnectDTO mapConnect(Connect connect) {
    ConnectDTO connectDTO = mapper.map(connect, ConnectDTO.class);
    connectDTO.setCreated(Date.from(connect.getCreated()));
    connectDTO.setUser1(Utility.mapUser(connect.getUser1()));
    connectDTO.setUser2(Utility.mapUser(connect.getUser2()));
    return connectDTO;
  }

  @Override
  public List<Long> getConnectedMentor(long menteeId) {
    List<Long> connected = new ArrayList<>();
    List<Connect> connects = connectRepository.findAll();
    for (Connect c : connects) {
      if (c.getUser1().getId() == menteeId) {
        connected.add(c.getUser2().getId());
      } else if (c.getUser2().getId() == menteeId) {
        connected.add(c.getUser1().getId());
      }
    }
    return connected;
  }

  @Override
  public void disconnect(long user1, long user2) {
    connectRepository.deleteConnection(user1, user2);
  }
}
