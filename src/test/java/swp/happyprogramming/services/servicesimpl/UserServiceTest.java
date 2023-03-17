package swp.happyprogramming.services.servicesimpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import swp.happyprogramming.application.services.UserService;
import swp.happyprogramming.application.dto.UserDTO;
import swp.happyprogramming.application.exception.auth.UserAlreadyExistException;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void registerNewUserAccount_newUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("test");
        userDTO.setEmail("testee" + Instant.now() + "@gmail.com");
        userDTO.setFirstName("");
        userDTO.setLastName("");
        try {
            userService.registerNewUserAccount(userDTO);
        } catch (UserAlreadyExistException e) {
            fail("There is an account with that email address: " + userDTO.getEmail());
        }
    }

    @Test
    void registerNewUserAccount_existedUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("test");
        userDTO.setEmail("testee062202@gmail.com");
        userDTO.setFirstName("");
        userDTO.setLastName("");
        try {
            userService.registerNewUserAccount(userDTO);
            fail("There is an account with that email address: " + userDTO.getEmail());
        } catch (UserAlreadyExistException e) {
            assertTrue(true);
        }
    }

    @Test
    void countUsersByRolesLike() {
    }

    @Test
    void testGetConnectionsById() {
    }

    @Test
    void findByEmail_invalidEmail() {
        String invalidEmailFormat = "invalidEmailFormat";
        assertNull(userService.findByEmail(invalidEmailFormat));
    }

    @Test
    void findByEmail_notExistEmail() {
        String notExistEmail = "notExistEmail@gmail.com";
        assertNull(userService.findByEmail(notExistEmail));
    }

    @Test
    void findByEmail_exist() {
        String existEmail = "testee062202@gmail.com";
        User user = userService.findByEmail(existEmail);
        assertNotNull(user);
        assertEquals(existEmail, user.getEmail());
    }

    @Test
    void findByEmail_empty() {
        String existEmail = "";
        User user = userService.findByEmail(existEmail);
        assertNull(user);
    }

    @Test
    void findByEmail_null() {
        String existEmail = null;
        User user = userService.findByEmail(existEmail);
        assertNull(user);
    }

    @Test
    void getUserById_null() {
        assertNull(userService.getUserById(null));
    }

    @Test
    void getUserById_negative() {
        assertNull(userService.getUserById((long) -1));
    }

    @Test
    void getUserById_notExist() {
        Long notExistId = Long.MAX_VALUE;
        assertNull(userService.getUserById(notExistId));
    }

    @Test
    void getUserById_exist() {
        Long existId = 12L;
        User user = userService.getUserById(existId);
        assertNotNull(user);
        assertEquals(existId, user.getId());
    }

    @Test
    void updateUserProfile() {
    }

    @Test
    void getRequestsByEmail() {
    }

    @Test
    void updateImage() {
    }

    @Test
    void getMentees() {
    }

    @Test
    void updateResetPasswordToken() {
    }

    @Test
    void getByResetPasswordToken() {
    }

    @Test
    void updatePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        long id = 12L;
        String password = "test";
        User user = userService.getUserById(id);
        userService.updatePassword(user, password);
        assertTrue(encoder.matches(password, user.getPassword()));
    }

    @Test
    void enableUser() {
        long userId = 12L;
        int expected = 1;
        userService.enableUser(userId);
        User user = userService.getUserById(userId);
        assertEquals(expected, (int) user.getStatus());
    }

    @Test
    void disableUser() {
        long userId = 12L;
        int expected = 0;
        userService.disableUser(userId);
        User user = userService.getUserById(userId);
        assertEquals(expected, (int) user.getStatus());
    }

    @Test
    void getMonthlyNewMentees() {
    }
}