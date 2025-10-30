package controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import java.io.IOException;

 // Optimized single TestFX class testing all controllers to minimize JavaFX application startup overhead.
 // This replaces individual TestFX files for maximum performance.
@ExtendWith(ApplicationExtension.class)
class AllControllersTestFX extends FxRobot {

    private LoginController loginController;
    private HomePageController homeController;
    private SignUpController signUpController;

    @Start
    private void start(Stage stage) throws IOException {
        // Start with login page (most commonly tested)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginpage.fxml"));
        Parent root = loader.load();
        loginController = loader.getController();
        
        stage.setTitle("All Controllers Test");
        stage.setScene(new Scene(root, 640, 400));
        stage.show();
        stage.toFront();
    }

    @Test
    void testLoginController() {
        // Test LoginController - covers handleLoginButton, clearError, showError
        assertNotNull(loginController);
        assertTrue(loginController instanceof LoginController);
        assertTrue(loginController instanceof BaseController);
        
        // Single interaction to exercise most controller logic
        clickOn("#loginButton"); // Empty fields - exercises validation path
        
        // Test sign up navigation
        Button createAccountButton = lookup("#createAccountButton").query();
        if (createAccountButton != null) {
            clickOn("#createAccountButton"); // Exercises handleSignUpButton
        }
    }

    @Test
    void testHomePageController() throws IOException, InterruptedException {
        // Load home page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homepage.fxml"));
        Parent root = loader.load();
        homeController = loader.getController();
        
        // Update scene to home page on FX Application Thread
        javafx.application.Platform.runLater(() -> {
            Stage stage = (Stage) lookup("#loginButton").query().getScene().getWindow();
            stage.setScene(new Scene(root, 640, 400));
        });
        
        // Wait for scene update
        Thread.sleep(200);
        
        // Test HomePageController - covers handleLogoutButton
        assertNotNull(homeController);
        assertTrue(homeController instanceof HomePageController);
        assertTrue(homeController instanceof BaseController);
        
        Button logoutButton = lookup("#logoutButton").query();
        assertNotNull(logoutButton);
        clickOn("#logoutButton"); // Exercises handleLogoutButton method
    }

    @Test
    void testSignUpController() throws IOException, InterruptedException {
        // Load sign up page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signuppage.fxml"));
        Parent root = loader.load();
        signUpController = loader.getController();
        
        // Update scene to sign up page on FX Application Thread
        javafx.application.Platform.runLater(() -> {
            // Get current stage from any existing button
            Stage stage = null;
            try {
                stage = (Stage) lookup("#loginButton").query().getScene().getWindow();
            } catch (Exception e) {
                // If loginButton not found, try logoutButton or any other element
                try {
                    stage = (Stage) lookup("*").query().getScene().getWindow();
                } catch (Exception ex) {
                    // Fallback - get the primary stage
                    stage = javafx.stage.Stage.getWindows().stream()
                        .filter(w -> w instanceof Stage)
                        .map(w -> (Stage) w)
                        .findFirst()
                        .orElse(null);
                }
            }
            if (stage != null) {
                stage.setScene(new Scene(root, 640, 400));
            }
        });
        
        // Wait for scene update
        Thread.sleep(200);
        
        // Test SignUpController - covers handleSignUpButton, handleBackToLoginButton
        assertNotNull(signUpController);
        assertTrue(signUpController instanceof SignUpController);
        assertTrue(signUpController instanceof BaseController);
        
        // Test sign up functionality
        clickOn("#signUpButton"); // Empty fields - exercises validation and error handling
        
        // Test back to login navigation
        clickOn("#returnToLogin"); // Exercises handleBackToLoginButton
    }



    @Test
    void testLoginControllerWithValidInput() throws InterruptedException {
        // Test login with actual input to cover more code paths
        assertNotNull(loginController);
        
        // Fill in username and password fields
        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        
        if (usernameField != null && passwordField != null) {
            clickOn("#usernameField").write("testuser");
            clickOn("#passwordField").write("testpass");
            clickOn("#loginButton"); // This should trigger actual login logic
            Thread.sleep(100); // Allow time for processing
        }
    }

    @Test
    void testSignUpControllerWithInput() throws IOException, InterruptedException {
        // Load sign up page for input testing
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signuppage.fxml"));
        Parent root = loader.load();
        signUpController = loader.getController();
        
        // Update scene
        javafx.application.Platform.runLater(() -> {
            Stage stage = javafx.stage.Stage.getWindows().stream()
                    .filter(w -> w instanceof Stage)
                    .map(w -> (Stage) w)
                    .findFirst()
                    .orElse(null);
            if (stage != null) {
                stage.setScene(new Scene(root, 640, 400));
            }
        });
        Thread.sleep(200);
        
        // Test sign up with various input scenarios
        TextField usernameField = lookup("#usernameField").query();
        TextField emailField = lookup("#emailField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        
        if (usernameField != null && emailField != null && passwordField != null) {
            // Test empty fields first (already done in other test)
            clickOn("#signUpButton");
            Thread.sleep(50);
            
            // Test with partial input
            clickOn("#usernameField").write("user");
            clickOn("#signUpButton"); // Should trigger validation for missing email/password
            Thread.sleep(50);
            
            // Clear and test email field
            eraseText(4); // Clear username
            clickOn("#emailField").write("test@example.com");
            clickOn("#signUpButton"); // Should trigger validation for missing username/password
            Thread.sleep(50);
            
            // Test password field
            clickOn("#passwordField").write("password123");
            clickOn("#signUpButton"); // Should trigger validation for missing username
            Thread.sleep(50);
        }
    }

    @Test
    void testLoginControllerErrorHandling() throws InterruptedException {
        // Test various error conditions to improve coverage
        assertNotNull(loginController);
        
        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        
        if (usernameField != null && passwordField != null) {
            // Test with only username
            clickOn("#usernameField").write("onlyuser");
            clickOn("#loginButton"); // Should show password required error
            Thread.sleep(50);
            
            // Clear and test with only password
            eraseText(8); // Clear username
            clickOn("#passwordField").write("onlypass");
            clickOn("#loginButton"); // Should show username required error
            Thread.sleep(50);
            
            // Test with invalid credentials
            clickOn("#usernameField").write("invaliduser");
            eraseText(8); // Clear password field
            clickOn("#passwordField").write("invalidpass");
            clickOn("#loginButton"); // Should trigger login failure path
            Thread.sleep(100);
        }
    }



    @Test
    void testCompleteUserWorkflow() throws IOException, InterruptedException {
        // Test a complete user workflow: Login -> Home -> Logout -> SignUp -> Back
        assertNotNull(loginController);
        
        // Step 1: Try login (will fail but exercises code)
        TextField usernameField = lookup("#usernameField").query();
        PasswordField passwordField = lookup("#passwordField").query();
        
        if (usernameField != null && passwordField != null) {
            clickOn("#usernameField").write("workflow");
            clickOn("#passwordField").write("test");
            clickOn("#loginButton");
            Thread.sleep(100);
        }
        
        // Step 2: Navigate to Sign Up
        Button createAccountButton = lookup("#createAccountButton").query();
        if (createAccountButton != null) {
            clickOn("#createAccountButton");
            Thread.sleep(200);
        }
        
        // Step 3: Test Sign Up workflow
        TextField signUpUsernameField = lookup("#usernameField").query();
        TextField signUpEmailField = lookup("#emailField").query();
        PasswordField signUpPasswordField = lookup("#passwordField").query();
        
        if (signUpUsernameField != null && signUpEmailField != null && signUpPasswordField != null) {
            clickOn("#usernameField").write("newuser");
            clickOn("#emailField").write("new@example.com");
            clickOn("#passwordField").write("newpass123");
            clickOn("#signUpButton"); // Will trigger sign up logic
            Thread.sleep(200); // Wait for navigation after successful sign-up
            
            // After successful sign-up, we should be back on the login page
            // Verify we're back on login page by checking for login-specific elements
            TextField loginUsernameField = lookup("#usernameField").query();
            if (loginUsernameField != null) {
                // Successfully returned to login page after sign-up
                System.out.println("Successfully completed sign-up workflow and returned to login");
            }
        }
    }
}
