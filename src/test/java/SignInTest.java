import com.example.local_history_archive.model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SignInTest {

    private static UserAccountDAO userAccountDAO;
    private static Connection connection;

    @BeforeAll
    public static void setUpAll() throws SQLException {
        // Use a memory-based SQLite database
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        connection.setAutoCommit(false);  // Set manual commit mode
        userAccountDAO = new UserAccountDAO();
        userAccountDAO.createTable();   // Create Table

        // Add a sample user to the database for sign-in testing
        UserAccount user = new UserAccount("testemail@example.com", "testuser", "password123", "Sample bio", "profilePic.jpg");
        userAccountDAO.newUser(user);
    }

    @AfterAll
    public static void tearDownAll() throws SQLException {
        // Delete data and close connection after testing
        userAccountDAO.deleteByEmail("testemail@example.com");
        userAccountDAO.deleteByEmail("nonexistent@example.com");
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

    // 1. Test successful sign-in with correct email and password
    @Test
    public void testSignInSuccess() {
        UserAccount user = userAccountDAO.signIn("testemail@example.com", "password123");

        // Check if the user can sign in with correct credentials
        assertNotNull(user, "User should be able to sign in with correct email and password.");
        assertEquals("testuser", user.getUsername(), "Username must match the expected value.");
        assertEquals("password123", user.getPassword(), "Password must match for successful sign-in.");

        // Success message
        System.out.println("testSignInSuccess: Sign-in succeeded for testemail@example.com");
    }

    // 2. Test failed sign-in with incorrect password
    @Test
    public void testSignInIncorrectPassword() {
        UserAccount user = userAccountDAO.signIn("testemail@example.com", "wrongpassword");

        // Ensure that sign-in fails when the password is incorrect
        assertNull(user, "User should not be able to sign in with incorrect password.");

        // Success message
        System.out.println("testSignInIncorrectPassword: Sign-in correctly failed for wrong password");
    }

    // 3. Test failed sign-in for a non-existent user
    @Test
    public void testSignInNonExistentUser() {
        UserAccount nonExistentUser = userAccountDAO.signIn("nonexistent@example.com", "password123");

        // Ensure that the non-existent user is not found
        assertNull(nonExistentUser, "Non-existent user should not be found for sign-in.");

        // Success message
        System.out.println("testSignInNonExistentUser: Sign-in correctly failed for non-existent user");
    }

    // 4. Test failed sign-in with an empty email field
    @Test
    public void testSignInEmptyFields() {
        UserAccount emptyUser = userAccountDAO.signIn("", "password123");

        // Ensure that sign-in fails with an empty email field
        assertNull(emptyUser, "User should not be found with an empty email.");

        // Success message
        System.out.println("testSignInEmptyFields: Sign-in correctly failed for empty email");
    }

    // 5. Test signing in with a non-existent email
    @Test
    public void testSignInNonExistentEmail() {
        UserAccount result = userAccountDAO.signIn("nonexistentemail@example.com", "anyPassword");

        // Ensure that the user is not found with a non-existent email
        assertNull(result, "User should not be found for a non-existent email.");

        // Success message
        System.out.println("testSignInNonExistentEmail: Sign-in correctly failed for non-existent email");
    }
}
