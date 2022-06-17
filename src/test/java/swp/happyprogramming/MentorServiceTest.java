package swp.happyprogramming;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import swp.happyprogramming.dto.MentorDTO;
import swp.happyprogramming.model.User;
import swp.happyprogramming.repository.IUserRepository;
import swp.happyprogramming.services.IMentorService;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MentorServiceTest {
    @Autowired
    private IMentorService mentorService;

    @Autowired
    private IUserRepository userRepository;

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
}
