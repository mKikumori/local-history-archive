import com.example.local_history_archive.controller.*;
import com.example.local_history_archive.model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EditProfileTest {

    private static UserAccountDAO userAccountDAO;
    private static Connection connection;
    private EditProfileController controller;
    private UserAccount mockUserAccount;

    @BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> {
            System.out.println("JavaFX Platform initialized");
        });
    }

    @BeforeAll
    public static void setUpAll() throws SQLException {
        // Use a memory-based SQLite database
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        connection.setAutoCommit(false);  // Set manual commit mode
        userAccountDAO = new UserAccountDAO();
        userAccountDAO.createTable();  // Create Table
        System.out.println("Database table created.");
    }

    @BeforeEach
    public void setUpEach() {
        controller = new EditProfileController();
        mockUserAccount = new UserAccount(1, "test@example.com", "testuser", "password", "bio", null);
        controller.userAccountDAO = userAccountDAO;
    }

    @Test
    public void testUpdateUserSuccess() throws SQLException {
        // Step 1: Create a new user
        UserAccount newUser = new UserAccount("initial_email@test.com", "initialUsername", "initialPassword", "initialBio", "initialPic");
        userAccountDAO.newUser(newUser);
        System.out.println("New user created: " + newUser.getUsername());

        // Step 2: Retrieve the user
        UserAccount createdUser = userAccountDAO.getByEmail("initial_email@test.com");
        assertNotNull(createdUser);
        System.out.println("User retrieved: " + createdUser.getUsername());

        // Step 3: Update the user's details
        createdUser.setUserEmail("updated_email@test.com");
        createdUser.setUsername("updatedUsername");
        createdUser.setPassword("updatedPassword");
        createdUser.setBio("updatedBio");
        createdUser.setProfilePic("updatedPic");

        // Update the user in the database
        userAccountDAO.updateUser(createdUser);
        System.out.println("User updated: " + createdUser.getUsername());

        // Step 4: Retrieve the updated user from the database
        UserAccount updatedUser = userAccountDAO.getById(createdUser.getUserId());
        assertNotNull(updatedUser);
        System.out.println("Updated user retrieved: " + updatedUser.getUsername());

        // Step 5: Assert that the user's information has been updated correctly
        assertEquals("updated_email@test.com", updatedUser.getUserEmail());
        assertEquals("updatedUsername", updatedUser.getUsername());
        assertEquals("updatedPassword", updatedUser.getPassword());
        assertEquals("updatedBio", updatedUser.getBio());
        assertEquals("updatedPic", updatedUser.getProfilePic());
        System.out.println("User update verified successfully.");

        connection.commit();  // 트랜잭션 커밋
    }

    @Test
    public void testDeleteUserSuccess() throws SQLException {
        // Step 1: Create a new user
        UserAccount newUser = new UserAccount("delete_test@test.com", "deleteTestUser", "testPassword", "testBio", "testPic");
        userAccountDAO.newUser(newUser);
        System.out.println("New user created: " + newUser.getUsername());

        // Step 2: Retrieve the user to ensure it exists
        UserAccount createdUser = userAccountDAO.getByEmail("delete_test@test.com");
        assertNotNull(createdUser);
        System.out.println("User retrieved: " + createdUser.getUsername());

        // Step 3: Delete the user
        userAccountDAO.deleteAccount(createdUser.getUserId());
        System.out.println("User deleted: " + createdUser.getUserId());

        // Step 4: Try to retrieve the deleted user, expecting a null result
        UserAccount deletedUser = userAccountDAO.getById(createdUser.getUserId());
        assertNull(deletedUser, "User should have been deleted but still exists.");
        System.out.println("Verified user was deleted successfully.");

        connection.commit();  // 트랜잭션 커밋
    }

    @AfterEach
    public void tearDownEach() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.rollback();  // 트랜잭션 롤백
            System.out.println("Transaction rolled back.");
        }
    }

    @AfterAll
    public static void tearDownAll() throws SQLException {
        if (connection != null) {
            connection.close();  // 연결 종료
            System.out.println("Database connection closed.");
        }
    }
}





