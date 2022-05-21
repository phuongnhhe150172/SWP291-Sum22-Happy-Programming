package swp.happyprogramming;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
<<<<<<< HEAD:src/test/java/swp391_sum22/happyprogramming/UserRepositoryTest.java
import swp391_sum22.happyprogramming.repository.IUserRepository;
import swp391_sum22.happyprogramming.model.User;
=======
import swp.happyprogramming.dao.IUserRepository;
import swp.happyprogramming.model.User;
>>>>>>> Develop:src/test/java/swp/happyprogramming/UserRepositoryTest.java

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
        user.setFullName("Pham Thi Van Anh");
        user.setEmail("AnhPTV@fpt.edu.vn");
        user.setPassword("A1234567890");
        user.setCreated(Date.from(Instant.now()));
        user.setModified(Date.from(Instant.now()));

        userRepository.save(user);
    }

    @Test
    public void testFindUserByEmail(){
        String email = "AnhPTV@fpt.edu.vn";
        User user = userRepository.findByEmail(email);

        Assertions.assertThat(user).isNotNull();
    }
}
