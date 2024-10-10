import com.example.local_history_archive.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

//import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class SignUpTest {

    private static UserAccountDAO userAccountDAO;
    private static Connection connection;

    @BeforeAll
    public static void setUpAll() throws SQLException {
        // Use a memory-based SQLite database
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        connection.setAutoCommit(false);  // Set manual commit mode
        userAccountDAO = new UserAccountDAO();
        userAccountDAO.createTable();  // Create Table
    }

    @AfterAll
    public static void tearDownAll() throws SQLException {
        // Delete data and close connection after testing
        userAccountDAO.deleteByEmail("newuser@example.com");
        userAccountDAO.deleteByEmail("duplicate@example.com");
        userAccountDAO.deleteByEmail("duplicateusername@example.com");
        userAccountDAO.deleteByEmail("user6@example.com");
        userAccountDAO.deleteByEmail("user5@example.com");
        userAccountDAO.deleteByEmail("user2@example.com");
        userAccountDAO.deleteByEmail("user3@example.com");
        userAccountDAO.deleteByEmail("");

        // commit transaction
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
        }

        // End connection
        if (connection != null) {
            connection.close();
        }
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        // Delete data from each email after testing
        userAccountDAO.deleteByEmail("newuser@example.com");
        userAccountDAO.deleteByEmail("duplicate@example.com");
        userAccountDAO.deleteByEmail("duplicateusername@example.com");
        userAccountDAO.deleteByEmail("user6@example.com");
        userAccountDAO.deleteByEmail("user5@example.com");
        userAccountDAO.deleteByEmail("user2@example.com");
        userAccountDAO.deleteByEmail("user3@example.com");
        userAccountDAO.deleteByEmail("");

        // commit transaction
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
        }
    }

    // 1. Test for successful add user
    @Test
    public void testSignUpSuccess() {
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

    // 4. Test for missing fields
    @Test
    public void testSignUpMissingFields() {
        // Missing Email
        UserAccount missingEmailUser = new UserAccount("", "user1", "password123", "This is a bio", "profilePic.jpg");
        boolean result = userAccountDAO.newUser(missingEmailUser);
        assertFalse(result, "Sign up should fail if email is missing.");

        // Missing Password
        UserAccount missingPasswordUser = new UserAccount("user2@example.com", "user2", "", "This is a bio", "profilePic.jpg");
        result = userAccountDAO.newUser(missingPasswordUser);
        assertFalse(result, "Sign up should fail if password is missing.");

        // Missing Username
        UserAccount missingUsernameUser = new UserAccount("user3@example.com", "", "password123", "This is a bio", "profilePic.jpg");
        result = userAccountDAO.newUser(missingUsernameUser);
        assertFalse(result, "Sign up should fail if username is missing.");
    }

    // 5. Test for invalid characters in username
    @Test
    public void testSignUpWithInvalidCharacters() {
        UserAccount invalidUser = new UserAccount("user5@example.com", "user'; DROP TABLE userAccounts;", "password123", "This is a bio", "profilePic.jpg");
        boolean result = userAccountDAO.newUser(invalidUser);
        assertFalse(result, "Sign up should fail if username contains invalid characters.");

        UserAccount anotherInvalidUser = new UserAccount("user6@example.com", "invalid@username", "password123", "This is a bio", "profilePic.jpg");
        result = userAccountDAO.newUser(anotherInvalidUser);
        assertFalse(result, "Sign up should fail if username contains forbidden characters like '@'.");
    }

    //6. Valid Email Checking
    @Test
    public void testSignUpWithValidEmailAddress() {
        UserAccount invalidEmail = new UserAccount("abc.abc.com","abb","123","","");
        boolean result = userAccountDAO.newUser(invalidEmail);
        assertFalse(result, "Sign up should fail if the email address is invalid.");
    }
}

