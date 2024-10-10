import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.local_history_archive.model.UserAccountDAO;
import com.example.local_history_archive.model.UserAccount;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PasswordRecoveryTest {

    private static UserAccountDAO userAccountDAO;
    private static Connection connection;

    @BeforeAll
    public static void setUpAll() throws SQLException {
        // Use a memory-based SQLite database
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        connection.setAutoCommit(false);  // Set manual commit mode
        userAccountDAO = new UserAccountDAO();
        userAccountDAO.createTable(); // Create Table
    }

    @AfterAll
    public static void tearDownAll() throws SQLException {
        // End connection
        if (connection != null) {
            connection.close();
        }
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        // Delete data and close connection after testing
        userAccountDAO.deleteByEmail("testemail@example.com");
        userAccountDAO.deleteByEmail("wrongemail@example.com");
        userAccountDAO.deleteByEmail("");

        // commit transaction
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
        }
    }

    // Test successful password recovery with correct email
    @Test
    public void testPasswordRecoverySuccess() throws SQLException {
        // Insert a sample user for testing password recovery
        UserAccount testUser = new UserAccount(
                "testemail@example.com",
                "testuser",
                "password123",
                "Sample bio",
                "profilePic.jpg"
        );
        userAccountDAO.newUser(testUser);  // Add the sample user

        String correctPassword = userAccountDAO.recoverPassword("testemail@example.com");
        assertNotNull(correctPassword, "Password should be recovered for a real user.");
        assertEquals("password123", correctPassword, "The password should match the expected value.");

        // Success message
        System.out.println("testPasswordRecoverySuccess: Password recovery succeeded for testemail@example.com");
    }

    // Test password recovery with incorrect email
    @Test
    public void testPasswordRecoveryIncorrectEmail() throws SQLException {
        // Insert a sample user
        UserAccount testUser = new UserAccount(
                "testemail@example.com",
                "testuser",
                "password123",
                "Sample bio",
                "profilePic.jpg"
        );
        userAccountDAO.newUser(testUser);  // Add the sample user

        // Test the recovery of the password with a fake email (not in the database)
        String wrongPassword = userAccountDAO.recoverPassword("wrongemail@example.com");
        assertNull(wrongPassword, "Password should be null for non-existent email.");

        // Success message
        System.out.println("testPasswordRecoveryIncorrectEmail: Password recovery correctly failed for wrongemail@example.com");
    }

    // Test password recovery with empty email field
    @Test
    public void testPasswordRecoveryEmptyFields() throws SQLException {
        String emptyUser = userAccountDAO.recoverPassword("");
        assertNull(emptyUser, "User should not be found with an empty email.");

        // Success message
        System.out.println("testPasswordRecoveryEmptyFields: Password recovery correctly failed for empty email");
    }

    // Test changing the password successfully
    @Test
    public void testChangePasswordSuccess() throws SQLException {
        // Insert a sample user for testing password change
        UserAccount testUser = new UserAccount(
                "testemail@example.com",
                "testuser",
                "password123",
                "Sample bio",
                "profilePic.jpg"
        );
        userAccountDAO.newUser(testUser);  // Add the sample user

        // Change the password for the existing user
        boolean isPasswordChanged = userAccountDAO.changePassword("testemail@example.com", "newPassword456");

        // Assert that the password change was successful
        assertTrue(isPasswordChanged, "Password should be changed successfully.");

        // Verify the new password works for sign-in
        UserAccount user = userAccountDAO.signIn("testemail@example.com", "newPassword456");
        assertNotNull(user, "User should be able to sign in with the new password.");
        assertEquals("testuser", user.getUsername(), "Username should match the expected value.");

        // Success message
        System.out.println("testChangePasswordSuccess: Password change succeeded for testemail@example.com");
    }

    // Test changing the password for a non-existent email
    @Test
    public void testChangePasswordNonExistentEmail() throws SQLException {
        // Try to change the password for an email not in the database
        boolean isPasswordChanged = userAccountDAO.changePassword("nonexistent@example.com", "newPassword456");

        // Assert that the password change should fail
        assertFalse(isPasswordChanged, "Password should not be changed for a non-existent email.");

        // Success message
        System.out.println("testChangePasswordNonExistentEmail: Password change correctly failed for nonexistent@example.com");
    }
}





