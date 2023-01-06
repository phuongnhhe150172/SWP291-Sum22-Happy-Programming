package swp.happyprogramming;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import swp.happyprogramming.domain.dto.ConnectDTO;
import swp.happyprogramming.domain.dto.MentorDTO;
import swp.happyprogramming.adapter.port.out.IConnectRepository;
import swp.happyprogramming.adapter.port.out.IUserRepository;
import swp.happyprogramming.application.usecase.IConnectService;
import swp.happyprogramming.application.usecase.IMentorService;

import java.util.List;

@SpringBootTest
public class MentorServiceTest {
    @Autowired
    private IMentorService mentorService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IConnectRepository connectRepository;

    @Autowired
    private IConnectService connectService;

    @Test
    public void testGetMentors() {
//        List<MentorDTO> list = mentorService.getMentors();
//        System.out.println(list.size());
//        Assertions.assertThat(list).isNotNull();
//        for (MentorDTO m:list) {
//            System.out.println(m.getLastName());
//        }
    }

    @Test
    public void testFindMentorByID(){
        long id = 12;
        MentorDTO m = mentorService.findMentor(id);
        Assertions.assertThat(m).isNotNull();
        System.out.println(m.getLastName());

//        Optional<User> u = userRepository.findById(id);
//        Assertions.assertThat(u).isNotNull();
//        System.out.println(u.get().getFirstName());
    }

    @Test
    public void testConnect(){
//        Optional<Connect> connect = connectRepository.findConnectByUser1IdAndUser2Id(23,22);
//        Connect c = connect.get();
//        System.out.println(c.getId());
//        System.out.println(c.getUser1().getFirstName());

//        List<Connect> c = connectRepository.findAll();
//        System.out.println(c.get(0).getId());
//        System.out.println(c.get(0).getUser1().getFirstName());

        List<ConnectDTO> c = connectService.findAllConnections();
        System.out.println(c.get(0).getId());
        System.out.println(c.get(0).getUser1().getFirstName());
    }

    @Test
    public void testConnectMap(){
        List<Long> c = connectService.getConnectedMentor(43);
        for (Long id:c
             ) {
            System.out.println(id);
        }
    }
}
