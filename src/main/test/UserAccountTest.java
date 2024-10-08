import com.example.local_history_archive.model.UserAccount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAccountTest {
    private UserAccount userAccount;
    @BeforeEach
    public void setUp(){
    userAccount = new UserAccount("user@exampletest.com", "test", "123", "bio", "null");
    }
    @Test
    public void testgetUserId(){
        userAccount.setUser_id(1);
        assertEquals(1,userAccount.getUserId());
    }
    @Test
    public void testgetPassword(){
        assertEquals("123", userAccount.getPassword());
    }
    @Test
    public void testgetUserEmail(){
        assertEquals("user@exampletest.com", userAccount.getUserEmail());
    }
    @Test
    public void testgetBio(){
        assertEquals("bio", userAccount.getBio());
    }
    @Test
    public void testgetProfilePic(){
        assertEquals("null", userAccount.getProfilePic());
    }
}