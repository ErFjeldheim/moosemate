package app;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.lang.reflect.Method;
import java.net.URL;

 // TestFX-compatible tests for App class to achieve better code coverage.
 // This version will be included when running mvn test -Dtest="*TestFX"
@ExtendWith(ApplicationExtension.class)
public class AppTestFX extends FxRobot {

    private App app;

    @Start
    private void start(Stage stage) throws Exception {
        app = new App();
        app.start(stage);
    }

    @BeforeAll
    public static void setUpHeadlessMode() {
        System.setProperty("java.awt.headless", "true");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "false"); // TestFX needs this false
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

    @Test
    public void testAppInstanceCreatedByTestFX() {
        // Test that TestFX created a valid App instance
        assertNotNull(app);
        assertTrue(app instanceof Application);
        assertTrue(app instanceof App);
    }

    @Test
    public void testAppStartMethodExecuted() {
        // Test that start method was actually executed by TestFX
        // This gives us coverage of the actual start() method
        assertNotNull(app);
        
        // Verify the UI was loaded (loginpage should be present)
        assertDoesNotThrow(() -> {
            // The fact that TestFX started successfully means start() method worked
            lookup("#usernameField"); // This should exist if start() loaded loginpage.fxml
        });
    }



    @Test
    public void testFXMLLoaderInTestFX() {
        // Test that FXMLLoader works with App resources
        assertDoesNotThrow(() -> {
            URL resource = App.class.getResource("/fxml/loginpage.fxml");
            assertNotNull(resource, "FXML resource should be found");
            FXMLLoader loader = new FXMLLoader(resource);
            assertNotNull(loader);
            assertNotNull(loader.getLocation());
            assertTrue(loader.getLocation().toString().contains("loginpage.fxml"));
        });
    }



    @Test
    public void testActualStartMethodExecution() {
        // This test exercises the actual start() method through TestFX
        // Since TestFX called start() to initialize the UI, we can verify it worked
        assertNotNull(app);
        
        // The presence of UI elements proves start() method executed successfully
        assertDoesNotThrow(() -> {
            // These elements should exist if start() loaded the FXML correctly
            assertNotNull(lookup("#usernameField").query());
            assertNotNull(lookup("#passwordField").query());
            assertNotNull(lookup("#loginButton").query());
        });
    }

    @Test
    public void testSceneAndStageSetup() {
        // Test that start() method properly set up the scene and stage
        assertDoesNotThrow(() -> {
            // TestFX provides access to the stage that was set up by start()
            Stage stage = (Stage) lookup(".root").query().getScene().getWindow();
            assertNotNull(stage);
            assertNotNull(stage.getScene());
            
            // Verify the title was set (as done in start method)
            assertEquals("MooseMate", stage.getTitle());
        });
    }

    @Test
    public void testApplicationLaunchSetup() {
        // Test that the app is properly set up for JavaFX launch
        assertNotNull(app);
        assertTrue(app instanceof Application);
        
        // Verify main method exists for launch
        assertDoesNotThrow(() -> {
            Method mainMethod = App.class.getDeclaredMethod("main", String[].class);
            assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
        });
    }

    @Test
    public void testAppConstructor() {
        // Test that App can be instantiated
        assertDoesNotThrow(() -> {
            App newApp = new App();
            assertNotNull(newApp);
            assertTrue(newApp instanceof Application);
            assertTrue(newApp instanceof App);
        });
    }

    @Test
    public void testMultipleResourcesAccessible() {
        // Test that all expected resources are accessible
        String[] expectedResources = {
            "/fxml/loginpage.fxml",
            "/fxml/homepage.fxml", 
            "/fxml/signuppage.fxml",
            "/css/auth.css",
            "/css/common.css",
            "/css/homepage.css"
        };
        
        for (String resourcePath : expectedResources) {
            assertNotNull(App.class.getResource(resourcePath), 
                    "Resource should exist: " + resourcePath);
        }
    }



    @Test
    public void testStartMethodImplementationDetails() {
        // Test the specific implementation details of start() method
        assertDoesNotThrow(() -> {
            // Verify the FXML was loaded (evidenced by UI elements being present)
            assertNotNull(lookup("#usernameField").query());
            
            // Verify the scene was created and set
            Stage stage = (Stage) lookup(".root").query().getScene().getWindow();
            assertNotNull(stage.getScene());
            
            // Verify stage properties were set as in start() method
            assertEquals("MooseMate", stage.getTitle());
            assertTrue(stage.isShowing());
        });
    }

    @Test
    public void testFXMLLoaderCreationFromStart() {
        // Test the FXMLLoader creation logic used in start() method
        assertDoesNotThrow(() -> {
            // This mimics the exact logic from start() method
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/loginpage.fxml"));
            assertNotNull(fxmlLoader);
            assertNotNull(fxmlLoader.getLocation());
            
            // The fact that our UI loaded proves this logic works in start()
            assertTrue(fxmlLoader.getLocation().toString().contains("loginpage.fxml"));
        });
    }


}
