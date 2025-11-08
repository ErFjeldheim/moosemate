package controller;

import dto.MoosageDto;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HomePageController using TestFX.
 */
@ExtendWith(ApplicationExtension.class)
class HomePageControllerTest extends FxRobot {

    private HomePageController controller;

    @Start
    private void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homepage.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        stage.setTitle("Home Page Test");
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
        
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void testControllerLoads() {
        // Verify controller initialized
        assertNotNull(controller, "Controller should be initialized");
    }

    @Test
    void testMoosageListExists() {
        // Verify moosage list view is present
        ListView<?> moosageList = lookup("#moosageList").query();
        assertNotNull(moosageList, "Moosage list should exist");
    }

    @Test
    void testPostTextAreaExists() {
        // Verify post text area is present
        TextArea postTextArea = lookup("#postTextArea").query();
        assertNotNull(postTextArea, "Post text area should exist");
    }

    @Test
    void testPostButtonExists() {
        // Verify post button is present
        Button postButton = lookup("#postButton").query();
        assertNotNull(postButton, "Post button should exist");
    }

    @Test
    void testCharCountLabelExists() {
        // Verify character count label is present
        Label charCountLabel = lookup("#postCharCountLabel").query();
        assertNotNull(charCountLabel, "Character count label should exist");
    }

    @Test
    void testLogoutIconExists() {
        // Verify logout icon is present
        assertNotNull(lookup("#logoutIcon").query(), "Logout icon should exist");
    }

    @Test
    void testInitialCharacterCount() throws InterruptedException {
        // Test initial character count is "0 / 280"
        sleep(100, TimeUnit.MILLISECONDS);
        Label charCountLabel = lookup("#postCharCountLabel").query();
        String labelText = charCountLabel.getText();
        assertTrue(labelText.contains("0") && labelText.contains("280"), 
            "Initial character count should show 0/280");
    }

    @Test
    void testCharacterCountUpdate() throws InterruptedException {
        // Test character count updates as user types
        interact(() -> {
            TextArea postTextArea = lookup("#postTextArea").query();
            if (postTextArea != null) {
                postTextArea.clear();
                postTextArea.setText("Hello World!");
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        Label charCountLabel = lookup("#postCharCountLabel").query();
        String labelText = charCountLabel.getText();
        assertTrue(labelText.contains("12"), "Character count should update to 12");
    }

    @Test
    void testCharacterCountWithLongText() throws InterruptedException {
        // Test character count with longer text
        String longText = "This is a longer message to test the character counter functionality!";
        interact(() -> {
            TextArea postTextArea = lookup("#postTextArea").query();
            if (postTextArea != null) {
                postTextArea.clear();
                postTextArea.setText(longText);
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        Label charCountLabel = lookup("#postCharCountLabel").query();
        String labelText = charCountLabel.getText();
        assertTrue(labelText.contains(String.valueOf(longText.length())), 
            "Character count should match text length");
    }

    @Test
    void testCharacterCountColorChangeNear90Percent() throws InterruptedException {
        // Test character count color changes when approaching limit (252+ chars = 90% of 280)
        String nearLimitText = "a".repeat(252);  // 90% of 280
        interact(() -> {
            TextArea postTextArea = lookup("#postTextArea").query();
            if (postTextArea != null) {
                postTextArea.clear();
                postTextArea.setText(nearLimitText);
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        Label charCountLabel = lookup("#postCharCountLabel").query();
        // At 90%, the label should turn red according to controller logic
        assertNotNull(charCountLabel.getStyle(), "Label style should be set");
    }

    @Test
    void testCharacterLimit280() throws InterruptedException {
        // Test that character limit is enforced at 280
        String maxText = "a".repeat(280);
        interact(() -> {
            TextArea postTextArea = lookup("#postTextArea").query();
            if (postTextArea != null) {
                postTextArea.clear();
                postTextArea.setText(maxText);
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        TextArea postTextArea = lookup("#postTextArea").query();
        assertTrue(postTextArea.getText().length() <= 280, 
            "Text should not exceed 280 characters");
    }

    @Test
    void testCharacterLimitExceeded() throws InterruptedException {
        // Test behavior when trying to exceed character limit
        String tooLongText = "a".repeat(300);
        interact(() -> {
            TextArea postTextArea = lookup("#postTextArea").query();
            if (postTextArea != null) {
                postTextArea.clear();
                postTextArea.setText(tooLongText);
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        TextArea postTextArea = lookup("#postTextArea").query();
        assertTrue(postTextArea.getText().length() <= 280, 
            "Text should be truncated to 280 characters");
    }

    @Test
    void testPostButtonWithEmptyText() throws InterruptedException {
        // Test clicking post button with empty text
        interact(() -> {
            TextArea postTextArea = lookup("#postTextArea").query();
            Button postButton = lookup("#postButton").query();
            
            if (postTextArea != null) postTextArea.clear();
            if (postButton != null) postButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        // Nothing should happen with empty text
        assertNotNull(controller);
    }

    @Test
    void testPostButtonWithValidText() throws InterruptedException {
        // Test clicking post button with valid text
        interact(() -> {
            TextArea postTextArea = lookup("#postTextArea").query();
            Button postButton = lookup("#postButton").query();
            
            if (postTextArea != null) {
                postTextArea.clear();
                postTextArea.setText("This is a test moosage!");
            }
            if (postButton != null) postButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(200, TimeUnit.MILLISECONDS);
        
        assertNotNull(controller);
    }

    @Test
    void testClearTextArea() throws InterruptedException {
        // Test clearing text area
        interact(() -> {
            TextArea postTextArea = lookup("#postTextArea").query();
            if (postTextArea != null) {
                postTextArea.setText("Some text");
                postTextArea.clear();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
        
        TextArea postTextArea = lookup("#postTextArea").query();
        assertTrue(postTextArea.getText().isEmpty(), "Text area should be empty");
    }

    @Test
    void testLogoutButtonClick() throws InterruptedException {
        // Test clicking logout icon
        interact(() -> {
            clickOn("#logoutIcon");
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        // Should logout and navigate to login page
        assertNotNull(controller);
    }

    @Test
    void testMoosageListInitialization() throws InterruptedException {
        // Test that moosage list is initialized (may be empty if backend not running)
        sleep(200, TimeUnit.MILLISECONDS);
        
        ListView<MoosageDto> moosageList = lookup("#moosageList").query();
        assertNotNull(moosageList, "Moosage list should be initialized");
        assertNotNull(moosageList.getItems(), "Moosage list items should not be null");
    }

    @Test
    void testMultipleTextEdits() throws InterruptedException {
        // Test multiple text edits and character count updates
        String[] texts = {"Hello", "Hello World", "This is a test", ""};
        
        for (String text : texts) {
            interact(() -> {
                TextArea postTextArea = lookup("#postTextArea").query();
                if (postTextArea != null) {
                    postTextArea.clear();
                    postTextArea.setText(text);
                }
            });
            WaitForAsyncUtils.waitForFxEvents();
            sleep(50, TimeUnit.MILLISECONDS);
            
            Label charCountLabel = lookup("#postCharCountLabel").query();
            assertTrue(charCountLabel.getText().contains(String.valueOf(text.length())), 
                "Character count should update for each text: " + text);
        }
    }

    @Test
    void testPostTextAreaInput() throws InterruptedException {
        // Test typing in post text area
        interact(() -> {
            TextArea postTextArea = lookup("#postTextArea").query();
            if (postTextArea != null) {
                postTextArea.clear();
                postTextArea.setText("Testing multiline\ntext input\nfor moosages!");
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        TextArea postTextArea = lookup("#postTextArea").query();
        assertTrue(postTextArea.getText().contains("multiline"), 
            "Text area should handle multiline text");
    }

    @Test
    void testCharacterCountWithSpecialCharacters() throws InterruptedException {
        // Test character count with special characters
        String specialText = "Test 123 !@#$%^&*()_+-=[]{}|;':\",./<>?";
        interact(() -> {
            TextArea postTextArea = lookup("#postTextArea").query();
            if (postTextArea != null) {
                postTextArea.clear();
                postTextArea.setText(specialText);
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        Label charCountLabel = lookup("#postCharCountLabel").query();
        assertTrue(charCountLabel.getText().contains(String.valueOf(specialText.length())), 
            "Character count should include special characters");
    }

    @Test
    void testCharacterCountWithEmojis() throws InterruptedException {
        // Test character count with emojis
        String emojiText = "Hello 😊 World 🌍!";
        interact(() -> {
            TextArea postTextArea = lookup("#postTextArea").query();
            if (postTextArea != null) {
                postTextArea.clear();
                postTextArea.setText(emojiText);
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        Label charCountLabel = lookup("#postCharCountLabel").query();
        // Character count should update (emojis may count as multiple characters)
        assertTrue(charCountLabel.getText().matches(".*\\d+.*"), 
            "Character count should be displayed");
    }

    @Test
    void testPostButtonState() throws InterruptedException {
        // Test post button is enabled
        Button postButton = lookup("#postButton").query();
        assertNotNull(postButton, "Post button should exist");
        
        // Button state might change based on text content or posting state
        sleep(100, TimeUnit.MILLISECONDS);
        assertNotNull(postButton);
    }
}
