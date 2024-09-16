import com.example.local_history_archive.model.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class SignUpTest {

    private UserAccountDAO userAccountDAO;

    @BeforeEach
    public void setUp(){
        // Create a table before testing, and initialise the testing environment
        userAccountDAO = new UserAccountDAO();
        userAccountDAO.createUserTable();
    }

    @AfterEach
    public void tearDown(){
        // After testing, the database close automatically
        userAccountDAO.close();
    }

    // 1. Test for successful add user
    @Test
    public void testSignUpSuccess(){
        UserAccount newUser = new UserAccount("newuser@example.com", "newuser", "password123", "bio", "profilePic.jpg");
        userAccountDAO.newUser(newUser);

        UserAccount retrievedUser = userAccountDAO.getByEmail("newuser@example.com");
        assertNotNull(retrievedUser, "유저가 성공적으로 추가되어야 합니다.");
        assertEquals("newuser@example.com", retrievedUser.getUserEmail(), "이메일이 일치해야 합니다.");
        assertEquals("newuser", retrievedUser.getUsername(), "유저명이 일치해야 합니다.");
    }

    // 2. 중복된 이메일로 회원가입 시도 시 오류 처리 (중복 추가되지 않음)
    @Test
    public void testSignUpDuplicateEmail() {
        UserAccount firstUser = new UserAccount("duplicate@example.com", "user1", "password123", "This is a bio", "profilePic.jpg");
        userAccountDAO.newUser(firstUser);

        // 중복된 이메일로 두 번째 유저를 추가
        UserAccount duplicateUser = new UserAccount("duplicate@example.com", "user2", "password456", "Another bio", "profilePic2.jpg");
        userAccountDAO.newUser(duplicateUser);

        // 같은 이메일로 추가된 유저가 없도록 확인
        List<UserAccount> allAccounts = userAccountDAO.getAll();
        long duplicateCount = allAccounts.stream()
                .filter(account -> account.getUserEmail().equals("duplicate@example.com"))
                .count();

        assertEquals(1, duplicateCount, "중복된 이메일은 두 번 추가되지 않아야 합니다.");
    }
}
