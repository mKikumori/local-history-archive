import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.local_history_archive.model.UserAccountDAO;  // Import UserAccountDAO
import com.example.local_history_archive.model.UserAccount;     // Import UserAccount

public class PasswordRecoveryTest {

    private UserAccountDAO userAccountDAO;

    @BeforeEach
    public void setup() {
        // Initialize the DAO and prepare the test environment
        userAccountDAO = new UserAccountDAO();
        userAccountDAO.createUserTable(); // Create the table again before each test

        // Insert a sample user for testing password recovery
        UserAccount testUser = new UserAccount(
                "testemail@example.com",
                "testuser",
                "password123",
                "Sample bio",
                "profilePic.jpg"
        );
        userAccountDAO.newUser(testUser);  // Add the sample user
    }

    @AfterEach
    public void tearDown() {
        // Close any resources or reset the state after each test
        userAccountDAO.close();
    }

    // Test successful password recovery with correct email
    @Test
    public void testPasswordRecoverySuccess() {
        String correctPassword = userAccountDAO.recoverPassword("testemail@example.com");
        assertNotNull(correctPassword, "Password should be recovered for a real user.");
        assertEquals("password123", correctPassword, "The password should match the expected value.");
    }

    // Test password recovery with incorrect email
    @Test
    public void testPasswordRecoveryIncorrectEmail() {
        // Test the recovery of the password with a fake email (not in the database)
        String wrongPassword = userAccountDAO.recoverPassword("wrongemail@example.com");
        assertNull(wrongPassword, "Password should be null for non-existent email.");
    }

    // Test password recovery with empty email field
    @Test
    public void testPasswordRecoveryEmptyFields() {
        String emptyUser = userAccountDAO.recoverPassword("");
        assertNull(emptyUser, "User should not be found with an empty email.");
    }
    // Test changing the password successfully
    @Test
    public void testChangePasswordSuccess() {
        // Change the password for the existing user
        boolean isPasswordChanged = userAccountDAO.changePassword("testemail@example.com", "newPassword456");

        // Assert that the password change was successful
        assertTrue(isPasswordChanged, "Password should be changed successfully.");

        // Verify the new password works for sign-in
        UserAccount user = userAccountDAO.signIn("testemail@example.com", "newPassword456");
        assertNotNull(user, "User should be able to sign in with the new password.");
        assertEquals("testuser", user.getUsername(), "Username should match the expected value.");
    }

    // Test changing the password for a non-existent email
    @Test
    public void testChangePasswordNonExistentEmail() {
        // Try to change the password for an email not in the database
        boolean isPasswordChanged = userAccountDAO.changePassword("nonexistent@example.com", "newPassword456");

        // Assert that the password change should fail
        assertFalse(isPasswordChanged, "Password should not be changed for a non-existent email.");
    }
}
