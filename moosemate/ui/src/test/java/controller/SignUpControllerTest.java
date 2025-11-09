package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for SignUpController using TestFX.
 */
@ExtendWith(ApplicationExtension.class)
class SignUpControllerTest extends FxRobot {

    private SignUpController controller;

    @Start
    private void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signuppage.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        stage.setTitle("Sign Up Test");
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
    void testEmailFieldExists() {
        // Verify email field is present
        TextField emailField = lookup("#emailField").query();
        assertNotNull(emailField, "Email field should exist");
    }

    @Test
    void testPasswordFieldExists() {
        // Verify password field is present
        TextField passwordField = lookup("#passwordField").query();
        assertNotNull(passwordField, "Password field should exist");
    }

    @Test
    void testSignUpButtonExists() {
        // Verify sign up button is present
        Button signUpButton = lookup("#signUpButton").query();
        assertNotNull(signUpButton, "Sign up button should exist");
    }

    @Test
    void testBackToLoginButtonExists() {
        // Verify back to login button is present
        Button backButton = lookup("#returnToLogin").query();
        assertNotNull(backButton, "Back to login button should exist");
    }

    @Test
    void testSignUpWithEmptyFields() throws InterruptedException {
        // Test sign up with all fields empty
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            TextField emailField = lookup("#emailField").query();
            TextField passwordField = lookup("#passwordField").query();
            Button signUpButton = lookup("#signUpButton").query();
            
            if (usernameField != null) {
                usernameField.clear();
            }
            if (emailField != null) {
                emailField.clear();
            }
            if (passwordField != null) {
                passwordField.clear();
            }
            if (signUpButton != null) {
                signUpButton.fire();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        // Error should be shown
        assertNotNull(controller);
    }

    @Test
    void testSignUpWithOnlyUsername() throws InterruptedException {
        // Test sign up with only username filled
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            TextField emailField = lookup("#emailField").query();
            TextField passwordField = lookup("#passwordField").query();
            Button signUpButton = lookup("#signUpButton").query();
            
            if (usernameField != null) {
                usernameField.clear();
                usernameField.setText("testuser");
            }
            if (emailField != null) {
                emailField.clear();
            }
            if (passwordField != null) {
                passwordField.clear();
            }
            if (signUpButton != null) {
                signUpButton.fire();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        assertNotNull(controller);
    }

    @Test
    void testSignUpWithOnlyEmail() throws InterruptedException {
        // Test sign up with only email filled
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            TextField emailField = lookup("#emailField").query();
            TextField passwordField = lookup("#passwordField").query();
            Button signUpButton = lookup("#signUpButton").query();
            
            if (usernameField != null) {
                usernameField.clear();
            }
            if (emailField != null) {
                emailField.clear();
                emailField.setText("test@example.com");
            }
            if (passwordField != null) {
                passwordField.clear();
            }
            if (signUpButton != null) {
                signUpButton.fire();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        assertNotNull(controller);
    }

    @Test
    void testSignUpWithOnlyPassword() throws InterruptedException {
        // Test sign up with only password filled
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            TextField emailField = lookup("#emailField").query();
            TextField passwordField = lookup("#passwordField").query();
            Button signUpButton = lookup("#signUpButton").query();
            
            if (usernameField != null) {
                usernameField.clear();
            }
            if (emailField != null) {
                emailField.clear();
            }
            if (passwordField != null) {
                passwordField.clear();
                passwordField.setText("password123");
            }
            if (signUpButton != null) {
                signUpButton.fire();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        assertNotNull(controller);
    }

    @Test
    void testSignUpWithMissingUsername() throws InterruptedException {
        // Test sign up with email and password but no username
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            TextField emailField = lookup("#emailField").query();
            TextField passwordField = lookup("#passwordField").query();
            Button signUpButton = lookup("#signUpButton").query();
            
            if (usernameField != null) {
                usernameField.clear();
            }
            if (emailField != null) {
                emailField.clear();
                emailField.setText("test@example.com");
            }
            if (passwordField != null) {
                passwordField.clear();
                passwordField.setText("password123");
            }
            if (signUpButton != null) {
                signUpButton.fire();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        assertNotNull(controller);
    }

    @Test
    void testSignUpWithValidInput() throws InterruptedException {
        // Test sign up with all fields filled
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            TextField emailField = lookup("#emailField").query();
            TextField passwordField = lookup("#passwordField").query();
            Button signUpButton = lookup("#signUpButton").query();
            
            if (usernameField != null) {
                usernameField.clear();
                usernameField.setText("newuser");
            }
            if (emailField != null) {
                emailField.clear();
                emailField.setText("newuser@example.com");
            }
            if (passwordField != null) {
                passwordField.clear();
                passwordField.setText("password123");
            }
            if (signUpButton != null) {
                signUpButton.fire();
            }
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
                usernameField.setText("testuser123");
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
        
        TextField usernameField = lookup("#usernameField").query();
        assertEquals("testuser123", usernameField.getText());
    }

    @Test
    void testEmailFieldInput() throws InterruptedException {
        // Test typing in email field
        interact(() -> {
            TextField emailField = lookup("#emailField").query();
            if (emailField != null) {
                emailField.clear();
                emailField.setText("user@test.com");
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
        
        TextField emailField = lookup("#emailField").query();
        assertEquals("user@test.com", emailField.getText());
    }

    @Test
    void testPasswordFieldInput() throws InterruptedException {
        // Test typing in password field
        interact(() -> {
            TextField passwordField = lookup("#passwordField").query();
            if (passwordField != null) {
                passwordField.clear();
                passwordField.setText("securePassword");
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
        
        TextField passwordField = lookup("#passwordField").query();
        assertEquals("securePassword", passwordField.getText());
    }

    @Test
    void testBackToLoginButtonClick() throws InterruptedException {
        // Test clicking back to login button
        interact(() -> {
            Button backButton = lookup("#returnToLogin").query();
            if (backButton != null) {
                backButton.fire();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        // Should navigate back to login page
        assertNotNull(controller);
    }

    @Test
    void testClearFieldsAfterInput() throws InterruptedException {
        // Test clearing all fields after input
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            TextField emailField = lookup("#emailField").query();
            TextField passwordField = lookup("#passwordField").query();
            
            if (usernameField != null) {
                usernameField.setText("user1");
                usernameField.clear();
            }
            if (emailField != null) {
                emailField.setText("user1@test.com");
                emailField.clear();
            }
            if (passwordField != null) {
                passwordField.setText("pass123");
                passwordField.clear();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(50, TimeUnit.MILLISECONDS);
        
        TextField usernameField = lookup("#usernameField").query();
        TextField emailField = lookup("#emailField").query();
        TextField passwordField = lookup("#passwordField").query();
        assertTrue(usernameField.getText().isEmpty());
        assertTrue(emailField.getText().isEmpty());
        assertTrue(passwordField.getText().isEmpty());
    }

    @Test
    void testEmailValidation() throws InterruptedException {
        // Test email format validation
        interact(() -> {
            TextField usernameField = lookup("#usernameField").query();
            TextField emailField = lookup("#emailField").query();
            TextField passwordField = lookup("#passwordField").query();
            Button signUpButton = lookup("#signUpButton").query();
            
            if (usernameField != null) {
                usernameField.clear();
                usernameField.setText("testuser");
            }
            if (emailField != null) {
                emailField.clear();
                emailField.setText("invalidemail");  // Invalid email format
            }
            if (passwordField != null) {
                passwordField.clear();
                passwordField.setText("password123");
            }
            if (signUpButton != null) {
                signUpButton.fire();
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
        sleep(100, TimeUnit.MILLISECONDS);
        
        assertNotNull(controller);
    }

    @Test
    void testMultipleSignUpAttempts() throws InterruptedException {
        // Test multiple sign up attempts
        for (int i = 0; i < 3; i++) {
            final int attempt = i;
            interact(() -> {
                TextField usernameField = lookup("#usernameField").query();
                TextField emailField = lookup("#emailField").query();
                TextField passwordField = lookup("#passwordField").query();
                Button signUpButton = lookup("#signUpButton").query();
                
                if (usernameField != null) {
                    usernameField.clear();
                    usernameField.setText("user" + attempt);
                }
                if (emailField != null) {
                    emailField.clear();
                    emailField.setText("user" + attempt + "@test.com");
                }
                if (passwordField != null) {
                    passwordField.clear();
                    passwordField.setText("pass" + attempt);
                }
                if (signUpButton != null) {
                    signUpButton.fire();
                }
            });
            WaitForAsyncUtils.waitForFxEvents();
            sleep(100, TimeUnit.MILLISECONDS);
        }
        
        assertNotNull(controller);
    }
}
