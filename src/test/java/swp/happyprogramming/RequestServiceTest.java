package swp.happyprogramming;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import swp.happyprogramming.dto.RequestDTO;
import swp.happyprogramming.model.Request;
import swp.happyprogramming.repository.IRequestRepository;
import swp.happyprogramming.services.IRequestService;

import java.util.List;

@SpringBootTest
public class RequestServiceTest {
    @Autowired
    private IRequestRepository requestRepository;

    @Autowired
    private IRequestService requestService;

    @Test
    public void testGetAllRequest(){
        List<Request> list = requestRepository.findAll();
        Assertions.assertThat(list).isNotNull();
        System.out.println(list.size());
    }

    @Test
    public void testGetRequestSent(){
        long id = 20;
        List<RequestDTO> list = requestService.getRequestSent(id); //error because of null in budget, skillID column in database
        Assertions.assertThat(list).isNotNull();
        System.out.println(list.size());
    }
}
