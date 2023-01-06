package swp.happyprogramming;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import swp.happyprogramming.domain.model.Request;
import swp.happyprogramming.domain.model.User;
import swp.happyprogramming.model.*;
import swp.happyprogramming.adapter.port.out.IConnectRepository;
import swp.happyprogramming.adapter.port.out.IRequestRepository;
import swp.happyprogramming.application.usecase.IRequestService;
import swp.happyprogramming.application.usecase.IUserService;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class RequestServiceTest {
    @Autowired
    private IRequestRepository requestRepository;

    @Autowired
    private IRequestService requestService;

    @Autowired
    private IConnectRepository connectRepository;

    @Autowired
    private IUserService userService;

    @Test
    void testGetAllRequest(){
        List<Request> list = requestRepository.findAll();
        Assertions.assertThat(list).isNotNull();
        System.out.println(list.size());
    }



    @Test
    public void testRequestPaging(){
        int pageNumber = 2;
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 2);
        Page<Request> page = requestRepository.findAll(pageRequest);
        System.out.println(page.getTotalPages());
        Assertions.assertThat(page).isNotNull();

        List<Request> requestList = page.stream().collect(Collectors.toList());
        for (Request r: requestList
             ) {
            System.out.println(r.getMentor().getFirstName());
            System.out.println(r.getMentee().getFirstName());
        }
    }

    @Test
    public void testGetRequestSent(){
        long id = 43;
        User user = userService.getUserById(id);


        List<Request> list = requestRepository.findRequestByMenteeId(id);


        Assertions.assertThat(list).isNotEmpty();
        System.out.println(list.get(0).getMentor().getFirstName());
    }
}
