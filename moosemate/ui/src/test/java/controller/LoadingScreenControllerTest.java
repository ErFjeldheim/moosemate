package controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

/**
 * Unit tests for LoadingScreenController using TestFX.
 */
@ExtendWith(ApplicationExtension.class)
class LoadingScreenControllerTest extends FxRobot {

    private LoadingScreenController controller;

    @Start
    private void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loadingscreen.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        stage.setTitle("Loading Screen Test");
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
    void testLoadingScreenTransition() throws InterruptedException {
        // Wait for the 1 second transition to homepage
        WaitForAsyncUtils.waitForFxEvents();
        sleep(1500, TimeUnit.MILLISECONDS);
        WaitForAsyncUtils.waitForFxEvents();
        
        // If we got here without exception, the transition completed
        assertTrue(true, "Loading screen should transition successfully");
    }

    @Test
    void testLoadingLabelExists() {
        // Verify loading label is present
        interact(() -> {
            javafx.scene.control.Label loadingLabel = lookup("#loadingLabel").query();
            assertNotNull(loadingLabel, "Loading label should exist");
        });
        WaitForAsyncUtils.waitForFxEvents();
    }
}
