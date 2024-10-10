import com.example.local_history_archive.model.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserUploadDAOTest {

    private static UserUploadDAO userUploadDAO;
    private static UserAccountDAO userAccountDAO;
    private static Connection connection;
    private static int fixedUserId;  // 고정된 유저 ID

    @BeforeAll
    public static void setUpAll() throws SQLException {
        // DatabaseConnection을 통해 동일한 연결을 사용
        connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        connection.setAutoCommit(false);  // 수동 커밋 모드 설정

        // 같은 데이터베이스 연결을 사용하는 DAO 인스턴스 생성
        userAccountDAO = new UserAccountDAO();
        userUploadDAO = new UserUploadDAO();

        userAccountDAO.createTable();  // 유저 테이블 생성
        userUploadDAO.createUploadTable();  // 업로드 테이블 생성

        UserAccount testUser = new UserAccount("testuser@example.com", "testuser", "password123", "Test bio", "profilePic.jpg");
        userAccountDAO.newUser(testUser);

        // 생성된 유저의 user_id 가져오기 (한 번만)
        fixedUserId = userAccountDAO.getUserIdByEmail("testuser@example.com");
        System.out.println("Fixed User ID: " + fixedUserId);

        connection.commit(); // 데이터가 데이터베이스에 반영되도록 커밋
    }

    @BeforeEach
    public void setUp() throws SQLException {
        // 각 테스트 전에 트랜잭션 시작
        connection.setSavepoint();  // 트랜잭션의 세이브포인트 설정 (원하는 시점으로 되돌리기 위해)
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // 각 테스트 후 롤백하여 데이터베이스에 영향을 남기지 않음
        connection.rollback();  // 세이브포인트나 트랜잭션 시작점으로 롤백
    }

    @AfterAll
    public static void tearDownAll() throws SQLException {
        // 모든 테스트가 끝난 후 연결 종료
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testNewUploadSuccess() {
        // 유저가 제대로 생성되었는지 확인
        assertTrue(userAccountDAO.userExists(fixedUserId), "User must exist before uploading.");
        System.out.println("User ID exists: " + fixedUserId);

        UserUpload newUpload = new UserUpload(1, fixedUserId, "Upload 1", "Category 1", "Image", "Test description", false, "imageData", "2024-10-01");
        userUploadDAO.newUpload(newUpload);

        UserUpload retrievedUpload = userUploadDAO.getUploadById(1);
        assertNotNull(retrievedUpload, "The upload must be added successfully.");
        assertEquals("Upload 1", retrievedUpload.getUploadName(), "Upload names must match.");
        assertEquals("Category 1", retrievedUpload.getUploadCategories(), "Upload categories must match.");
    }

//    // 2. Test for failed upload with non-existent user
//    @Test
//    public void testUploadWithNonExistentUser() {
//        UserUpload newUpload = new UserUpload(999, 999, "Invalid Upload", "Category", "Image", "Invalid upload description", false, "imageData", "2024-10-01");
//        userUploadDAO.newUpload(newUpload);
//
//        UserUpload retrievedUpload = userUploadDAO.getUploadById(999);
//        assertNull(retrievedUpload, "Upload should not be added if user does not exist.");
//    }
//
//    // 3. Test for updating an upload
//    @Test
//    public void testUpdateUpload() {
//        // 새로운 업로드 추가
//        UserUpload newUpload = new UserUpload(1, 1, "Upload to Update", "Category", "Image", "Description before update", false, "imageData", "2024-10-01");
//        userUploadDAO.newUpload(newUpload);
//
//        // 업로드 업데이트
//        newUpload.setUploadName("Updated Upload");
//        newUpload.setUploadDescription("Updated description");
//        newUpload.setPinned(true);
//        userUploadDAO.updateUpload(newUpload);
//
//        UserUpload updatedUpload = userUploadDAO.getUploadById(1);
//        assertNotNull(updatedUpload, "The upload should still exist after update.");
//        assertEquals("Updated Upload", updatedUpload.getUploadName(), "Upload name should be updated.");
//        assertEquals("Updated description", updatedUpload.getUploadDescription(), "Upload description should be updated.");
//        assertTrue(updatedUpload.isPinned(), "Upload should be pinned.");
//    }
//
//    // 4. Test for deleting an upload
//    @Test
//    public void testDeleteUpload() {
//        // 새로운 업로드 추가
//        UserUpload newUpload = new UserUpload(1, 1, "Upload to Delete", "Category", "Image", "Description", false, "imageData", "2024-10-01");
//        userUploadDAO.newUpload(newUpload);
//
//        // 업로드 삭제
//        userUploadDAO.deleteUpload(1);
//
//        UserUpload deletedUpload = userUploadDAO.getUploadById(1);
//        assertNull(deletedUpload, "The upload should be deleted successfully.");
//    }
//
//    // 5. Test retrieving all uploads
//    @Test
//    public void testAllUploads() {
//        // 두 개의 업로드 추가
//        UserUpload upload1 = new UserUpload(1, 1, "First Upload", "Category 1", "Image", "First description", false, "imageData1", "2024-10-01");
//        UserUpload upload2 = new UserUpload(2, 1, "Second Upload", "Category 2", "Image", "Second description", false, "imageData2", "2024-10-01");
//        userUploadDAO.newUpload(upload1);
//        userUploadDAO.newUpload(upload2);
//
//        // 모든 업로드 가져오기
//        List<UserUpload> allUploads = userUploadDAO.allUploads();
//        assertEquals(2, allUploads.size(), "There should be two uploads.");
//        assertEquals("First Upload", allUploads.get(0).getUploadName(), "First upload's name should match.");
//        assertEquals("Second Upload", allUploads.get(1).getUploadName(), "Second upload's name should match.");
//    }
//
//    // 6. Test retrieving image data by upload ID
//    @Test
//    public void testGetImageDataByUploadId() {
//        UserUpload upload = new UserUpload(1, 1, "Upload with Image", "Category", "Image", "Description", false, "imageData123", "2024-10-01");
//        userUploadDAO.newUpload(upload);
//
//        String imageData = userUploadDAO.getImageDataByUploadId(1);
//        assertNotNull(imageData, "Image data should be retrieved.");
//        assertEquals("imageData123", imageData, "Image data should match.");
//    }
}



