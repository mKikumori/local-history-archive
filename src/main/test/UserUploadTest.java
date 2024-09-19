import com.example.local_history_archive.model.UserUpload;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserUploadTest {

    private UserUpload userUpload;

    @BeforeEach
    public void setUp() {
        userUpload = new UserUpload(101, "Historical Photo", "Culture", "Image", "A rare cultural artifact", true, "imageData123");
    }

    @Test
    public void testGetUserId() {
        assertEquals(101, userUpload.getUserId());
    }

    @Test
    public void testGetUploadName() {
        assertEquals("Historical Photo", userUpload.getUploadName());
    }

    @Test
    public void testGetUploadCategories() {
        assertEquals("Culture", userUpload.getUploadCategories());
    }

    @Test
    public void testGetUploadType() {
        assertEquals("Image", userUpload.getUploadType());
    }

    @Test
    public void testGetUploadDescription() {
        assertEquals("A rare cultural artifact", userUpload.getUploadDescription());
    }

    @Test
    public void testIsPinned() {
        assertTrue(userUpload.isPinned());
    }

    @Test
    public void testGetImageData() {
        assertEquals("imageData123", userUpload.getImageData());
    }
}
