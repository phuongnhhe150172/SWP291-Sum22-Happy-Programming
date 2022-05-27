package swp.happyprogramming;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import swp.happyprogramming.repository.IUserRepository;
import swp.happyprogramming.model.User;

import java.time.Instant;
import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {
    @Autowired
    private IUserRepository userRepository;

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
        String email = "PhuongNHHE150172@gmail.com";
        User user = userRepository.findByEmail(email);
        Assertions.assertThat(user).isNotNull();
    }

    @Test
    public void testCountUserByRole(){
        String role = "ROLE_MENTOR";
        int total = userRepository.countUsersByRolesLike(role);
    }
}
