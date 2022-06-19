package swp.happyprogramming;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import swp.happyprogramming.dto.RequestDTO;
import swp.happyprogramming.model.Pagination;
import swp.happyprogramming.model.Request;
import swp.happyprogramming.model.Role;
import swp.happyprogramming.model.User;
import swp.happyprogramming.repository.IRequestRepository;
import swp.happyprogramming.services.IRequestService;

import java.util.List;
import java.util.stream.Collectors;

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
        Pagination<RequestDTO> list = requestService.getRequestSent(id , 1); //error because of null in budget, skillID column in database
        Assertions.assertThat(list).isNotNull();
        System.out.println(list.getPaginatedList().size());
    }

    @Test
    public void testRequestPaging(){
        int pageNumber = 1;
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 2);
        Page<Request> page = requestRepository.findAll(pageRequest);
        System.out.println(page.getTotalPages());
        Assertions.assertThat(page).isNotNull();

        List<Request> requestList = page.stream().collect(Collectors.toList());
        for (Request r: requestList
             ) {
            System.out.println(r.getMentorId());
            System.out.println(r.getMenteeId());
            System.out.println(r.getBudget());
        }
    }

}
