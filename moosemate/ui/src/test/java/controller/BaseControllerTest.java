package controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import javafx.scene.control.Label;
import javafx.stage.Stage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
class BaseControllerTest {

    private BaseController controller;
    private Label errorLabel;

    @Start
    private void start(Stage stage) {
        stage.show();
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new BaseController();
        errorLabel = new Label();
        
        // Use reflection to set the private errorLabel field
        Field field = BaseController.class.getDeclaredField("errorLabel");
        field.setAccessible(true);
        field.set(controller, errorLabel);
    }

    @Test
    void testShowError() {
        controller.showError("Error message");
        
        assertEquals("Error: Error message", errorLabel.getText());
        assertTrue(errorLabel.isVisible());
        assertTrue(errorLabel.getStyleClass().contains("error-label"));
    }

    @Test
    void testShowError_EmptyMessage() {
        controller.showError("");
        
        assertEquals("Error: ", errorLabel.getText());
        assertTrue(errorLabel.isVisible());
        assertTrue(errorLabel.getStyleClass().contains("error-label"));
    }

    @Test
    void testShowSuccess() {
        controller.showSuccess("Success message");
        
        assertEquals("Success: Success message", errorLabel.getText());
        assertTrue(errorLabel.isVisible());
        assertTrue(errorLabel.getStyleClass().contains("success-label"));
    }

    @Test
    void testShowSuccess_EmptyMessage() {
        controller.showSuccess("");
        
        assertEquals("Success: ", errorLabel.getText());
        assertTrue(errorLabel.isVisible());
        assertTrue(errorLabel.getStyleClass().contains("success-label"));
    }

    @Test
    void testClearError() {
        // First show an error
        controller.showError("Test error");
        assertTrue(errorLabel.isVisible());
        
        // Then clear it
        controller.clearError();
        
        assertEquals("", errorLabel.getText());
        assertFalse(errorLabel.isVisible());
        assertTrue(errorLabel.getStyleClass().isEmpty());
    }

    @Test
    void testShowErrorReplacesSuccess() {
        controller.showSuccess("Success");
        assertTrue(errorLabel.getStyleClass().contains("success-label"));
        
        controller.showError("Error");
        
        assertEquals("Error: Error", errorLabel.getText());
        assertTrue(errorLabel.getStyleClass().contains("error-label"));
        assertFalse(errorLabel.getStyleClass().contains("success-label"));
    }

    @Test
    void testShowSuccessReplacesError() {
        controller.showError("Error");
        assertTrue(errorLabel.getStyleClass().contains("error-label"));
        
        controller.showSuccess("Success");
        
        assertEquals("Success: Success", errorLabel.getText());
        assertTrue(errorLabel.getStyleClass().contains("success-label"));
        assertFalse(errorLabel.getStyleClass().contains("error-label"));
    }

    @Test
    void testShowError_WithNullLabel() throws Exception {
        // Set errorLabel to null
        Field field = BaseController.class.getDeclaredField("errorLabel");
        field.setAccessible(true);
        field.set(controller, null);
        
        // Should not throw exception
        assertDoesNotThrow(() -> controller.showError("Error"));
    }

    @Test
    void testShowSuccess_WithNullLabel() throws Exception {
        // Set errorLabel to null
        Field field = BaseController.class.getDeclaredField("errorLabel");
        field.setAccessible(true);
        field.set(controller, null);
        
        // Should not throw exception
        assertDoesNotThrow(() -> controller.showSuccess("Success"));
    }

    @Test
    void testClearError_WithNullLabel() throws Exception {
        // Set errorLabel to null
        Field field = BaseController.class.getDeclaredField("errorLabel");
        field.setAccessible(true);
        field.set(controller, null);
        
        // Should not throw exception
        assertDoesNotThrow(() -> controller.clearError());
    }
}
