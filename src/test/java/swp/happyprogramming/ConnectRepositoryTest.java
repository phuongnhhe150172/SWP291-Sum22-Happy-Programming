package swp.happyprogramming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import swp.happyprogramming.dto.ConnectDTO;
import swp.happyprogramming.model.Connect;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.repository.IConnectRepository;
import swp.happyprogramming.services.IConnectService;

import java.util.List;


@SpringBootTest
public class ConnectRepositoryTest {
    @Autowired
    private IConnectRepository connectRepository;

    @Autowired
    private IConnectService connectService;

    @Test
    public void getConnections(){
        List<Connect> connects = connectRepository.findAll();
        for (Connect c :
                connects) {
            System.out.println(c.getCreated());
        }
    }

    @Test
    public void getConnectsFromService() {
        List<ConnectDTO> connectDTOS = connectService.findAllConnections();
        for (ConnectDTO c :
                connectDTOS) {
            System.out.println(c.getCreated());
        }
    }

    @Test
    public void getConnectPaging(){
        Pagination<ConnectDTO> connectDTOPagination = connectService.findAllConnections(1);
        List<ConnectDTO> connectDTOS = connectDTOPagination.getPaginatedList();
        for (ConnectDTO c :
                connectDTOS) {
            System.out.println(c.getUser1().getProfileId());
        }
    }
}
