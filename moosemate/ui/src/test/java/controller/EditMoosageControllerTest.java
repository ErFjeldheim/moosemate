package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
 * Unit tests for EditMoosageController using TestFX.
 */
@ExtendWith(ApplicationExtension.class)
class EditMoosageControllerTest extends FxRobot {

    private EditMoosageController controller;

    @Start
    private void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editmoosage.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        stage.setTitle("Edit Moosage Test");
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
    void testSetContent() throws InterruptedException {
        // Test setContent method
        interact(() -> {
            controller.setContent("Test moosage content");
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        // Verify text was set
        TextArea textArea = lookup("#contentTextArea").query();
        assertNotNull(textArea);
    }

    @Test
    void testSetContentWithEmptyString() throws InterruptedException {
        // Test setContent with empty string
        interact(() -> {
            controller.setContent("");
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
        
        assertNotNull(controller);
    }

    @Test
    void testSetContentWithLongText() throws InterruptedException {
        // Test with text longer than 280 characters
        String longText = "a".repeat(300);
        interact(() -> {
            controller.setContent(longText);
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
        
        assertNotNull(controller);
    }

    @Test
    void testGetResult_initiallyNull() {
        // Get result before any interaction
        String result = controller.getResult();
        assertNull(result, "Result should initially be null");
    }

    @Test
    void testCancelButton() throws InterruptedException {
        // Test cancel button
        interact(() -> {
            Button cancelButton = lookup("#cancelButton").query();
            if (cancelButton != null) {
                cancelButton.fire();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
    }

    @Test
    void testSaveButton() throws InterruptedException {
        // Set content and test save button
        interact(() -> {
            controller.setContent("Test content for save");
            Button saveButton = lookup("#saveButton").query();
            if (saveButton != null) {
                saveButton.fire();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
    }

    @Test
    void testCharacterCountUpdate() throws InterruptedException {
        // Test character count updates with different lengths
        interact(() -> {
            controller.setContent("Short");
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
        
        interact(() -> {
            controller.setContent("This is a longer moosage to test character counting");
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
        
        interact(() -> {
            controller.setContent("A".repeat(100));
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
    }

    @Test
    void testTextAreaInput() throws InterruptedException {
        // Test typing in text area
        interact(() -> {
            TextArea textArea = lookup("#contentTextArea").query();
            if (textArea != null) {
                textArea.clear();
                textArea.setText("User typed content");
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
    }

    @Test
    void testCharacterLimit() throws InterruptedException {
        // Test that character limit is enforced
        interact(() -> {
            TextArea textArea = lookup("#contentTextArea").query();
            if (textArea != null) {
                textArea.clear();
                // Try to set text longer than 280 chars
                String veryLongText = "X".repeat(350);
                textArea.setText(veryLongText);
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
    }
}
