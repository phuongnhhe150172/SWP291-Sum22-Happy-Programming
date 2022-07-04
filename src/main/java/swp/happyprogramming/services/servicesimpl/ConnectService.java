package swp.happyprogramming.services.servicesimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import swp.happyprogramming.dto.ConnectDTO;
import swp.happyprogramming.dto.RequestDTO;
import swp.happyprogramming.model.Connect;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Request;
import swp.happyprogramming.repository.IConnectRepository;
import swp.happyprogramming.services.IConnectService;
import swp.happyprogramming.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ConnectService implements IConnectService {
    @Autowired
    private IConnectRepository connectRepository;

    ModelMapper mapper = new ModelMapper();

    @Override
    public Connect findConnectByUser1AndUser2(long user1Id, long user2Id) {
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

    @Override
    public Pagination<ConnectDTO> findAllConnections(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 5);
        Page<Connect> page = connectRepository.findAll(pageRequest);
        int totalPages = page.getTotalPages();
        List<Connect> connects = page.getContent();
        List<ConnectDTO> connectDTOS = connects
                .stream()
                .map(connect -> mapper.map(connect, ConnectDTO.class))
                .collect(Collectors.toList());
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        return new Pagination<>(connectDTOS, pageNumbers);
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


}
