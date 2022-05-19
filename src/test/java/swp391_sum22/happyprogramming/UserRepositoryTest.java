package swp391_sum22.happyprogramming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import swp391_sum22.happyprogramming.dao.IUserRepository;
import swp391_sum22.happyprogramming.model.User;

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
    public void testCreateUser(){
        User user = new User();
        user.setFullName("Nguyen Hong Phuong");
        user.setEmail("PhuongNHHE150172");
        user.setPassword("A1234567890");
        user.setCreated(Date.from(Instant.now()));
        user.setModified(Date.from(Instant.now()));

        userRepository.save(user);
    }
}