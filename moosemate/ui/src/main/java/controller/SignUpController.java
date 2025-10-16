package controller;

import dto.ApiResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.ApiClient;

public class SignUpController extends BaseController {
    
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    private final ApiClient apiClient;

    public SignUpController() {
        this.apiClient = new ApiClient();
    }
    
    @FXML
    private void handleSignUpButton(ActionEvent event) {
        // Clear any previous error messages
        clearError(); 
        
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        
        try {
            // Validate input
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showError("All fields are required");
                return;
            }
            
            // Call REST API
            ApiResponse<?> response = apiClient.signUp(username, email, password);
            
            if (response.isSuccess()) {
                navigateToOtherPageWithSuccess(event, "/fxml/loginpage.fxml", "Login", 
                        "Account created successfully! Please log in.");
            } else {
                showError(response.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error handling signup: " + e.getMessage());
            showError("Unable to connect to server. Please make sure the REST API is up and running.");
        }
    }
    
    @FXML
    private void handleBackToLoginButton(ActionEvent event) {
        try {
            navigateToOtherPage(event, "/fxml/loginpage.fxml", "Login");
        } catch (Exception e) {
            System.err.println("Error... navigating back to login page: " + e.getMessage());
        }
    }
}
