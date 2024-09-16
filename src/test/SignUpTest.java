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
        assertNotNull(retrievedUser, "The user must be added successfully.");
        assertEquals("newuser@example.com", retrievedUser.getUserEmail(), "Emails must match.");
        assertEquals("newuser", retrievedUser.getUsername(), "Usernames must match.");
    }

    // 2. Error handling when attempting to sign up with a duplicate email (duplicates are not added)
    @Test
    public void testSignUpDuplicateEmail() {
        UserAccount firstUser = new UserAccount("duplicate@example.com", "user1", "password123", "This is a bio", "profilePic.jpg");
        userAccountDAO.newUser(firstUser);

        // Add a second user with a duplicate email
        UserAccount duplicateUser = new UserAccount("duplicate@example.com", "user2", "password456", "Another bio", "profilePic2.jpg");
        userAccountDAO.newUser(duplicateUser);

        // Make sure no users are added with the same email address
        List<UserAccount> allAccounts = userAccountDAO.getAll();
        long duplicateCount = allAccounts.stream()
                .filter(account -> account.getUserEmail().equals("duplicate@example.com"))
                .count();

        assertEquals(1, duplicateCount, "Duplicate emails should not be added twice.");
    }

    // 3. Error handling when attempting to sign up with a duplicate username (duplicates are not added)
    @Test
    public void testSignUpDuplicateUsername() {
        UserAccount firstUser = new UserAccount("duplicate@example.com", "user1", "password123", "This is a bio", "profilePic.jpg");
        userAccountDAO.newUser(firstUser);

        // Add a second user with a duplicate username
        UserAccount duplicateUser = new UserAccount("duplicateusername@example.com", "user1", "password456", "Another bio", "profilePic2.jpg");
        userAccountDAO.newUser(duplicateUser);

        // Make sure no users are added with the same username
        List<UserAccount> allAccounts = userAccountDAO.getAll();
        long duplicateCount = allAccounts.stream()
                .filter(account -> account.getUsername().equals("user1"))
                .count();

        assertEquals(1, duplicateCount, "Duplicate username should not be added twice.");
    }
}
