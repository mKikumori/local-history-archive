import com.example.local_history_archive.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignInTest {

    private UserAccountDAO userAccountDAO;

    @BeforeEach
    public void setUp() {
        // Initialize the in-memory database and create the user table
        userAccountDAO = new UserAccountDAO();
        userAccountDAO.createUserTable();

        // Add a sample user to the database for sign-in testing
        UserAccount user = new UserAccount("testemail@example.com", "testuser", "password123", "Sample bio", "profilePic.jpg");
        userAccountDAO.newUser(user);
    }

    @AfterEach
    public void tearDown() {
        // Close the DAO connection after each test
        userAccountDAO.close();
    }

    // 1. Test successful sign-in with correct email and password
    @Test
    public void testSignInSuccess() {
        UserAccount user = userAccountDAO.getByEmail("testemail@example.com");

        // Check if the user can sign in with correct credentials
        assertNotNull(user, "User should be found by email.");
        assertEquals("password123", user.getPassword(), "Password must match for successful sign-in.");
    }

    // 2. Test failed sign-in with incorrect password
    @Test
    public void testSignInIncorrectPassword() {
        UserAccount user = userAccountDAO.getByEmail("testemail@example.com");

        // Ensure that sign-in fails when the password is incorrect
        assertNotNull(user, "User should be found by email.");
        assertNotEquals("wrongpassword", user.getPassword(), "Password mismatch should cause sign-in failure.");
    }

    // 3. Test failed sign-in for a non-existent user
    @Test
    public void testSignInNonExistentUser() {
        UserAccount nonExistentUser = userAccountDAO.getByEmail("nonexistent@example.com");

        // Ensure that the non-existent user is not found
        assertNull(nonExistentUser, "Non-existent user should not be found.");
    }

    // 4. Test failed sign-in with an empty email field
    @Test
    public void testSignInEmptyFields() {
        UserAccount emptyUser = userAccountDAO.getByEmail("");

        // Ensure that sign-in fails with an empty email field
        assertNull(emptyUser, "User should not be found with an empty email.");
    }

}