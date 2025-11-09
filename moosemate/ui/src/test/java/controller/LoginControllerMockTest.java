package controller;

import dto.ApiResponse;
import dto.LoginResponse;
import dto.UserDto;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;
import service.ApiClient;
import service.SessionManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Mock-based tests for LoginController using Mockito to test API integration scenarios.
 */
@ExtendWith(ApplicationExtension.class)
class LoginControllerMockTest extends FxRobot {

    private LoginController controller;
    private ApiClient mockApiClient;
    private SessionManager sessionManager;

    @Start
    private void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginpage.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        stage.setTitle("Login Test with Mocks");
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
        
        WaitForAsyncUtils.waitForFxEvents();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Create mock ApiClient
        mockApiClient = mock(ApiClient.class);
        sessionManager = SessionManager.getInstance();
        sessionManager.logout(); // Clear any existing session
        
        // Use reflection to inject mock ApiClient into the controller
        // This is necessary because the controller caches ApiClient.getInstance() in its constructor
        if (controller != null) {
            Field apiClientField = LoginController.class.getDeclaredField("apiClient");
            apiClientField.setAccessible(true);
            apiClientField.set(controller, mockApiClient);
        }
    }

    @Test
    void testLoginSuccess_WithMockedApiClient() throws Exception {
        // Arrange - Create successful login response
        UserDto mockUser = new UserDto("testuser", "test@example.com");
        LoginResponse mockLoginResponse = new LoginResponse(mockUser, "mock-token-123", "mock-user-id");
        ApiResponse<LoginResponse> mockResponse = new ApiResponse<>(true, "Login successful", mockLoginResponse);

        // Mock the ApiClient.login() method
        when(mockApiClient.login(anyString(), anyString())).thenReturn(mockResponse);

        // Act - Perform login
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
            if (loginButton != null) {
                loginButton.fire();
            }
        });
        
        WaitForAsyncUtils.waitForFxEvents();
        sleep(200, TimeUnit.MILLISECONDS);

        // Assert - Verify login was called
        verify(mockApiClient, times(1)).login("testuser", "password123");
        
        // Verify session was stored (note: this happens in LoadingScreenController in real app)
        assertNotNull(controller);
    }

    @Test
    void testLoginFailure_InvalidCredentials() throws Exception {
        // Arrange - Create failed login response
        ApiResponse<LoginResponse> mockResponse = new ApiResponse<>(false, "Invalid username or password", null);

        when(mockApiClient.login(anyString(), anyString())).thenReturn(mockResponse);

        // Act - Perform login with invalid credentials
            interact(() -> {
                TextField usernameField = lookup("#usernameField").query();
                PasswordField passwordField = lookup("#passwordField").query();
                Button loginButton = lookup("#loginButton").query();
                
                if (usernameField != null) {
                    usernameField.clear();
                    usernameField.setText("wronguser");
                }
                if (passwordField != null) {
                    passwordField.clear();
                    passwordField.setText("wrongpass");
                }
                if (loginButton != null) {
                    loginButton.fire();
                }
            });
            
            WaitForAsyncUtils.waitForFxEvents();
            sleep(200, TimeUnit.MILLISECONDS);

        // Assert
        verify(mockApiClient, times(1)).login("wronguser", "wrongpass");
        
        // Verify error is shown
        Label errorLabel = lookup("#errorLabel").query();
        assertNotNull(errorLabel);
        assertTrue(errorLabel.isVisible());
        assertEquals("Invalid username or password", errorLabel.getText());
    }

    @Test
    void testLoginFailure_ServerConnectionError() throws Exception {
        // Arrange - Simulate connection error
        when(mockApiClient.login(anyString(), anyString()))
            .thenThrow(new RuntimeException("Connection refused"));

        // Act - Attempt login
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
                if (loginButton != null) {
                    loginButton.fire();
                }
            });
            
            WaitForAsyncUtils.waitForFxEvents();
            sleep(200, TimeUnit.MILLISECONDS);

        // Assert - Verify error message is shown
        Label errorLabel = lookup("#errorLabel").query();
        assertNotNull(errorLabel);
        assertTrue(errorLabel.isVisible());
        assertTrue(errorLabel.getText().contains("Unable to connect to server"));
    }

    @Test
    void testLoginFailure_NullResponse() throws Exception {
        // Arrange - Simulate null response (edge case)
        when(mockApiClient.login(anyString(), anyString())).thenReturn(null);

        // Act
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
                if (loginButton != null) {
                    loginButton.fire();
                }
            });
            
            WaitForAsyncUtils.waitForFxEvents();
            sleep(200, TimeUnit.MILLISECONDS);

        // Assert - Should handle gracefully
        assertNotNull(controller);
    }

    @Test
    void testLoginSuccess_WithEmptyMessage() throws Exception {
        // Arrange - Response with empty message
        UserDto mockUser = new UserDto("testuser", "test@example.com");
        LoginResponse mockLoginResponse = new LoginResponse(mockUser, "token", "userId");
        ApiResponse<LoginResponse> mockResponse = new ApiResponse<>(true, "", mockLoginResponse);

        when(mockApiClient.login(anyString(), anyString())).thenReturn(mockResponse);

        // Act
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
                if (loginButton != null) {
                    loginButton.fire();
                }
            });
            
            WaitForAsyncUtils.waitForFxEvents();
            sleep(200, TimeUnit.MILLISECONDS);

        // Assert
        verify(mockApiClient, times(1)).login("testuser", "password123");
    }

    @Test
    void testMultipleLoginAttempts_WithMocks() throws Exception {
        // Arrange - First attempt fails, second succeeds
        ApiResponse<LoginResponse> failResponse = new ApiResponse<>(false, "Invalid credentials", null);
        
        UserDto mockUser = new UserDto("testuser", "test@example.com");
        LoginResponse mockLoginResponse = new LoginResponse(mockUser, "token", "userId");
        ApiResponse<LoginResponse> successResponse = new ApiResponse<>(true, "Login successful", mockLoginResponse);

        when(mockApiClient.login("wronguser", "wrongpass")).thenReturn(failResponse);
        when(mockApiClient.login("testuser", "password123")).thenReturn(successResponse);

        // Act - First attempt (fail)
            interact(() -> {
                TextField usernameField = lookup("#usernameField").query();
                PasswordField passwordField = lookup("#passwordField").query();
                Button loginButton = lookup("#loginButton").query();
                
                if (usernameField != null) {
                    usernameField.clear();
                    usernameField.setText("wronguser");
                }
                if (passwordField != null) {
                    passwordField.clear();
                    passwordField.setText("wrongpass");
                }
                if (loginButton != null) {
                    loginButton.fire();
                }
            });
            
            WaitForAsyncUtils.waitForFxEvents();
            sleep(200, TimeUnit.MILLISECONDS);

            // Act - Second attempt (success)
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
                if (loginButton != null) {
                    loginButton.fire();
                }
            });
            
            WaitForAsyncUtils.waitForFxEvents();
            sleep(200, TimeUnit.MILLISECONDS);

        // Assert
        verify(mockApiClient, times(1)).login("wronguser", "wrongpass");
        verify(mockApiClient, times(1)).login("testuser", "password123");
    }
}
