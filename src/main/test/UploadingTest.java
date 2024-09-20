import com.example.local_history_archive.model.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class UploadingTest {

    private static UserUploadDAO userUploadDAO;

    @BeforeAll
    public static void setUpAll(){
        userUploadDAO = new UserUploadDAO();
        userUploadDAO.createUploadTable();
    }

    @AfterAll
    public static void tearDownAll(){
        // After testing, the database close automatically
        userUploadDAO.close();
    }

    // 1. Test to verify that correct data is stored in string
    @Test
    public void testToString() {
        UserUpload upload = new UserUpload(1, 101, "Artifact Image", "History", "Image", "An important historical artifact", true, "imageData123");
        String expected = "UserUpload{upload_id=1, user_id=101, upload_name='Artifact Image', upload_categories='History', upload_type='Image', upload_description='An important historical artifact', is_pinned=true', image_data=imageData123}";

        assertEquals(expected, upload.toString());
    }

    // 2. Test for successful add uploading data
    @Test
    public void testUploadSuccess() {
        // Given: Prepare new upload data.
        UserUpload newUpload = new UserUpload(2, "Test Upload", "Category1", "Type1", "This is a test description", false, "image_data_sample");
        UserUploadDAO userUploadDAO = new UserUploadDAO();

        // When: Insert the upload data into the database.
        userUploadDAO.newUpload(newUpload);

        // Then: Retrieve the inserted data and check if it matches.
        List<UserUpload> allUploads = userUploadDAO.allUploads();

        // Get the last inserted data from the upload list and compare it.
        UserUpload retrievedUpload = allUploads.get(allUploads.size() - 1);

        assertEquals(newUpload.getUploadName(), retrievedUpload.getUploadName());
        assertEquals(newUpload.getUploadCategories(), retrievedUpload.getUploadCategories());
        assertEquals(newUpload.getUploadType(), retrievedUpload.getUploadType());
        assertEquals(newUpload.getUploadDescription(), retrievedUpload.getUploadDescription());
        assertEquals(newUpload.isPinned(), retrievedUpload.isPinned());
        assertEquals(newUpload.getImageData(), retrievedUpload.getImageData());

        // Print success message to the console
        System.out.println("Upload test passed: The data has been successfully uploaded and verified.");
    }

    // 3. Image Format test
    @Test
    public void testGetUserID() {
        // Given: Prepare upload data with an invalid user ID.
        UserUpload newUpload = new UserUpload(1, "Test Upload", "Category1", "Type1", "This is a test description", false, "image_data_sample");
        UserUploadDAO userUploadDAO = new UserUploadDAO();

        // When: Check if the user ID exists in the database.
        int userId = userUploadDAO.getUserIdByUploadId(newUpload.getUploadId());

        // Then: Since the user does not exist, the result should be -1.
        assertEquals(-1, userId);

        // Print success message to the console
        System.out.println("User ID test passed: No user found with the specified ID.");
    }
}
