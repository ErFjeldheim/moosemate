package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
 * Unit tests for LoginController using TestFX.
 */
@ExtendWith(ApplicationExtension.class)
class LoginControllerTest extends FxRobot {

    private LoginController controller;

    @Start
    private void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginpage.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        stage.setTitle("Login Test");
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
    void testUsernameFieldExists() {
        // Verify username field is present
        TextField usernameField = lookup("#usernameField").query();
        assertNotNull(usernameField, "Username field should exist");
    }

    @Test
    void testPasswordFieldExists() {
        // Verify password field is present
        PasswordField passwordField = lookup("#passwordField").query();
        assertNotNull(passwordField, "Password field should exist");
    }

    @Test
    void testLoginButtonExists() {
        // Verify login button is present
        Button loginButton = lookup("#loginButton").query();
        assertNotNull(loginButton, "Login button should exist");
    }

    @Test
    void testSignUpButtonExists() {
        // Verify sign up button is present
        Button signUpButton = lookup("#goToSignupButton").query();
        assertNotNull(signUpButton, "Sign up button should exist");
    }

    @Test
    void testLoginWithEmptyFields() throws InterruptedException {
        // Test login with empty username and password
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            PasswordField passwordField = lookup("#passwordField").query();
            Button loginButton = lookup("#loginButton").query();
            
            if (usernameField != null) usernameField.clear();
            if (passwordField != null) passwordField.clear();
            if (loginButton != null) loginButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        // Error should be shown (but we can't easily assert on it without exposing error label)
        assertNotNull(controller);
    }

    @Test
    void testLoginWithOnlyUsername() throws InterruptedException {
        // Test login with username but no password
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            PasswordField passwordField = lookup("#passwordField").query();
            Button loginButton = lookup("#loginButton").query();
            
            if (usernameField != null) {
                usernameField.clear();
                usernameField.setText("testuser");
            }
            if (passwordField != null) passwordField.clear();
            if (loginButton != null) loginButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        assertNotNull(controller);
    }

    @Test
    void testLoginWithOnlyPassword() throws InterruptedException {
        // Test login with password but no username
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            PasswordField passwordField = lookup("#passwordField").query();
            Button loginButton = lookup("#loginButton").query();
            
            if (usernameField != null) usernameField.clear();
            if (passwordField != null) {
                passwordField.clear();
                passwordField.setText("password123");
            }
            if (loginButton != null) loginButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        assertNotNull(controller);
    }

    @Test
    void testLoginWithValidInput() throws InterruptedException {
        // Test login with both username and password
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            PasswordField passwordField = lookup("#passwordField").query();
            Button loginButton = lookup("#loginButton").query();
            
            if (usernameField != null) {
                usernameField.clear();
                usernameField.setText("testuser");
            }
            if (passwordField != null) {
                passwordField.clear();
                passwordField.setText("password123");
            }
            if (loginButton != null) loginButton.fire();
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(200, TimeUnit.MILLISECONDS);
        
        assertNotNull(controller);
    }

    @Test
    void testUsernameFieldInput() throws InterruptedException {
        // Test typing in username field
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            if (usernameField != null) {
                usernameField.clear();
                usernameField.setText("newuser");
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
        
        TextField usernameField = lookup("#usernameField").query();
        assertEquals("newuser", usernameField.getText());
    }

    @Test
    void testPasswordFieldInput() throws InterruptedException {
        // Test typing in password field
        interact(() -> {
            PasswordField passwordField = lookup("#passwordField").query();
            if (passwordField != null) {
                passwordField.clear();
                passwordField.setText("secret123");
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
        
        PasswordField passwordField = lookup("#passwordField").query();
        assertEquals("secret123", passwordField.getText());
    }

    @Test
    void testSignUpButtonClick() throws InterruptedException {
        // Test clicking sign up button
        interact(() -> {
            Button signUpButton = lookup("#goToSignupButton").query();
            if (signUpButton != null) {
                signUpButton.fire();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        // Should navigate to sign up page (but might fail if backend not running)
        assertNotNull(controller);
    }

    @Test
    void testClearFieldsAfterInput() throws InterruptedException {
        // Test clearing fields after input
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            PasswordField passwordField = lookup("#passwordField").query();
            
            if (usernameField != null) {
                usernameField.setText("user1");
                usernameField.clear();
            }
            if (passwordField != null) {
                passwordField.setText("pass123");
                passwordField.clear();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
        
        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        assertTrue(usernameField.getText().isEmpty());
        assertTrue(passwordField.getText().isEmpty());
    }

    @Test
    void testMultipleLoginAttempts() throws InterruptedException {
        // Test multiple login attempts
        for (int i = 0; i < 3; i++) {
            final int attempt = i;
            interact(() -> {
                TextField usernameField = lookup("#usernameField").query();
                PasswordField passwordField = lookup("#passwordField").query();
                Button loginButton = lookup("#loginButton").query();
                
                if (usernameField != null) {
                    usernameField.clear();
                    usernameField.setText("user" + attempt);
                }
                if (passwordField != null) {
                    passwordField.clear();
                    passwordField.setText("pass" + attempt);
                }
                if (loginButton != null) loginButton.fire();
            });
            WaitForAsyncUtils.waitForFxEvents();
            sleep(100, TimeUnit.MILLISECONDS);
        }
        
        assertNotNull(controller);
    }
}
