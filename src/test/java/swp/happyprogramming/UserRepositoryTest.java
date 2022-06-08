package swp.happyprogramming;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import swp.happyprogramming.dto.UserDTO;
import swp.happyprogramming.repository.IUserRepository;
import swp.happyprogramming.model.User;
import swp.happyprogramming.services.IUserService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {
    @Autowired
    private IUserRepository userRepository;


    @Autowired private IUserService userService;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setFirstName("Nguyen Hong");
        user.setLastName("Phuong");
        user.setEmail("PhuongNHHE150172@fpt.edu.vn");
        user.setFirstName("Nguyen Hong");
        user.setLastName("Phuong");
        user.setEmail("PhuongNHHE150172@gmail.com");
        user.setPassword("A1234567890");
        user.setCreated(Date.from(Instant.now()));
        user.setModified(Date.from(Instant.now()));
        userRepository.save(user);
    }

    @Test
    public void testFindUserByEmail() {
        String email = "test@example.com.vn";
        User user = userRepository.findByEmail(email);
        System.out.println(user.getAddress().getWard().getDistrict().getName());
    }

    @Test
    public void testCountUserByRole(){
        String role = "ROLE_MENTOR";
        int total = userRepository.countUsersByRolesLike(role);
    }

    @Test
    public void testShowAllMentees(){
        List<UserDTO> userDTOS = userService.findAllMentees();
        System.out.println(userDTOS.size());
    }
}
