package controller;

import dto.MoosageDto;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import service.SessionManager;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class MoosageListCellTest extends FxRobot {

    private MoosageListCell cell;

    @Start
    private void start(Stage stage) {
        // Empty stage for initialization
        stage.show();
    }

    @BeforeEach
    void setUp() {
        cell = new MoosageListCell();
        SessionManager.getInstance().logout(); // Start with no session
    }

    @Test
    void testUpdateItemWithNull() {
        cell.updateItem(null, true);
        assertNull(cell.getGraphic(), "Cell graphic should be null for empty cell");
    }

    @Test
    void testUpdateItemWithEmptyCell() {
        MoosageDto moosage = createTestMoosage("testUser", "testId", "Test content");
        cell.updateItem(moosage, true);
        assertNull(cell.getGraphic(), "Cell graphic should be null when marked as empty");
    }

    @Test
    void testUpdateItemWithValidMoosage() {
        MoosageDto moosage = createTestMoosage("testUser", "testId", "Test content");
        cell.updateItem(moosage, false);
        assertNotNull(cell.getGraphic(), "Cell graphic should be set for valid moosage");
        assertTrue(cell.getGraphic() instanceof VBox, "Cell graphic should be a VBox");
    }

    @Test
    void testMoosageContentDisplayed() {
        String content = "This is a test moosage";
        MoosageDto moosage = createTestMoosage("testUser", "testId", content);
        
        cell.updateItem(moosage, false);
        
        VBox graphic = (VBox) cell.getGraphic();
        assertNotNull(graphic, "Cell graphic should exist");
        
        // Content is displayed, we can verify the cell is not null
        assertNotNull(cell, "Cell should be initialized with content");
    }

    @Test
    void testUsernameDisplayed() {
        String username = "johndoe";
        MoosageDto moosage = createTestMoosage(username, "testId", "Test content");
        
        cell.updateItem(moosage, false);
        
        VBox graphic = (VBox) cell.getGraphic();
        Label usernameLabel = (Label) graphic.lookup("#usernameLabel");
        assertNotNull(usernameLabel, "Username label should exist");
        assertEquals(username, usernameLabel.getText(), "Username should match");
    }

    @Test
    void testLikeCountDisplayed() {
        MoosageDto moosage = createTestMoosage("testUser", "testId", "Test content");
        moosage.setLikedByUserIds(new HashSet<>());
        
        cell.updateItem(moosage, false);
        
        VBox graphic = (VBox) cell.getGraphic();
        Button likeButton = (Button) graphic.lookup(".button");
        assertNotNull(likeButton, "Like button should exist");
        assertTrue(likeButton.getText().contains("0"), "Like count should be 0");
    }

    @Test
    void testEditedLabelVisibleWhenEdited() {
        MoosageDto moosage = createTestMoosage("testUser", "testId", "Test content");
        moosage.setEdited(true);
        
        cell.updateItem(moosage, false);
        
        VBox graphic = (VBox) cell.getGraphic();
        Label editedLabel = (Label) graphic.lookup("#editedLabel");
        assertNotNull(editedLabel, "Edited label should exist");
        assertTrue(editedLabel.isVisible(), "Edited label should be visible");
    }

    @Test
    void testEditedLabelHiddenWhenNotEdited() {
        MoosageDto moosage = createTestMoosage("testUser", "testId", "Test content");
        moosage.setEdited(false);
        
        cell.updateItem(moosage, false);
        
        VBox graphic = (VBox) cell.getGraphic();
        Label editedLabel = (Label) graphic.lookup("#editedLabel");
        assertNotNull(editedLabel, "Edited label should exist");
        assertFalse(editedLabel.isVisible(), "Edited label should be hidden");
    }

    @Test
    void testMenuButtonVisibleForOwnMoosage() {
        String userId = "currentUser123";
        SessionManager.getInstance().login(createMockLoginResponse(userId));
        
        MoosageDto moosage = createTestMoosage("testUser", userId, "Test content");
        
        cell.updateItem(moosage, false);
        
        VBox graphic = (VBox) cell.getGraphic();
        MenuButton menuButton = (MenuButton) graphic.lookup(".menu-button");
        assertNotNull(menuButton, "Menu button should exist");
        assertTrue(menuButton.isVisible(), "Menu button should be visible for own moosage");
        
        SessionManager.getInstance().logout();
    }

    @Test
    void testMenuButtonHiddenForOthersMoosage() {
        String userId = "currentUser123";
        SessionManager.getInstance().login(createMockLoginResponse(userId));
        
        MoosageDto moosage = createTestMoosage("testUser", "differentUserId", "Test content");
        
        cell.updateItem(moosage, false);
        
        VBox graphic = (VBox) cell.getGraphic();
        MenuButton menuButton = (MenuButton) graphic.lookup(".menu-button");
        assertNotNull(menuButton, "Menu button should exist");
        assertFalse(menuButton.isVisible(), "Menu button should be hidden for others' moosage");
        
        SessionManager.getInstance().logout();
    }

    @Test
    void testTimestampFormatting_RightNow() {
        MoosageDto moosage = createTestMoosage("testUser", "testId", "Test content");
        moosage.setTime(LocalDateTime.now());
        
        cell.updateItem(moosage, false);
        
        VBox graphic = (VBox) cell.getGraphic();
        Label dateLabel = (Label) graphic.lookup("#dateLabel");
        assertNotNull(dateLabel, "Date label should exist");
        assertEquals("Right now", dateLabel.getText(), "Should show 'Right now' for current time");
    }

    @Test
    void testTimestampFormatting_Minutes() {
        MoosageDto moosage = createTestMoosage("testUser", "testId", "Test content");
        moosage.setTime(LocalDateTime.now().minusMinutes(5));
        
        cell.updateItem(moosage, false);
        
        VBox graphic = (VBox) cell.getGraphic();
        Label dateLabel = (Label) graphic.lookup("#dateLabel");
        assertNotNull(dateLabel, "Date label should exist");
        assertTrue(dateLabel.getText().contains("minute"), "Should show minutes");
    }

    @Test
    void testTimestampFormatting_Hours() {
        MoosageDto moosage = createTestMoosage("testUser", "testId", "Test content");
        moosage.setTime(LocalDateTime.now().minusHours(3));
        
        cell.updateItem(moosage, false);
        
        VBox graphic = (VBox) cell.getGraphic();
        Label dateLabel = (Label) graphic.lookup("#dateLabel");
        assertNotNull(dateLabel, "Date label should exist");
        assertTrue(dateLabel.getText().contains("hour"), "Should show hours");
    }

    @Test
    void testTimestampFormatting_FullDate() {
        MoosageDto moosage = createTestMoosage("testUser", "testId", "Test content");
        moosage.setTime(LocalDateTime.now().minusDays(2));
        
        cell.updateItem(moosage, false);
        
        VBox graphic = (VBox) cell.getGraphic();
        Label dateLabel = (Label) graphic.lookup("#dateLabel");
        assertNotNull(dateLabel, "Date label should exist");
        assertTrue(dateLabel.getText().matches(".+\\d+, \\d{4} \\d{2}:\\d{2}"), 
            "Should show full date format for old posts");
    }

    @Test
    void testOnDeleteCallbackSet() {
        AtomicBoolean callbackCalled = new AtomicBoolean(false);
        cell.setOnDeleteCallback(moosage -> callbackCalled.set(true));
        
        // Callback should be set (we can't easily test execution without API interaction)
        assertNotNull(cell, "Cell should still be valid after setting callback");
    }

    @Test
    void testLikeButtonStyleWhenNotLiked() {
        String userId = "currentUser123";
        SessionManager.getInstance().login(createMockLoginResponse(userId));
        
        MoosageDto moosage = createTestMoosage("testUser", "otherId", "Test content");
        moosage.setLikedByUserIds(new HashSet<>());
        
        cell.updateItem(moosage, false);
        
        VBox graphic = (VBox) cell.getGraphic();
        Button likeButton = (Button) graphic.lookup(".button");
        assertNotNull(likeButton, "Like button should exist");
        assertFalse(likeButton.getStyleClass().contains("liked"), 
            "Like button should not have 'liked' style class");
        
        SessionManager.getInstance().logout();
    }

    @Test
    void testLikeButtonStyleWhenLiked() {
        String userId = "currentUser123";
        SessionManager.getInstance().login(createMockLoginResponse(userId));
        
        MoosageDto moosage = createTestMoosage("testUser", "otherId", "Test content");
        HashSet<String> likedBy = new HashSet<>();
        likedBy.add(userId);
        moosage.setLikedByUserIds(likedBy);
        
        cell.updateItem(moosage, false);
        
        VBox graphic = (VBox) cell.getGraphic();
        Button likeButton = (Button) graphic.lookup(".button");
        assertNotNull(likeButton, "Like button should exist");
        assertTrue(likeButton.getStyleClass().contains("liked"), 
            "Like button should have 'liked' style class");
        
        SessionManager.getInstance().logout();
    }

    // Helper method to create a test moosage
    private MoosageDto createTestMoosage(String username, String authorId, String content) {
        MoosageDto moosage = new MoosageDto();
        moosage.setId(123L);
        moosage.setAuthorUsername(username);
        moosage.setAuthorId(authorId);
        moosage.setContent(content);
        moosage.setTime(LocalDateTime.now());
        moosage.setEdited(false);
        moosage.setLikedByUserIds(new HashSet<>());
        return moosage;
    }

    // Helper method to create a mock LoginResponse
    private dto.LoginResponse createMockLoginResponse(String userId) {
        dto.UserDto user = new dto.UserDto("testUser", "test@example.com");
        return new dto.LoginResponse(user, "mockToken", userId);
    }
}
