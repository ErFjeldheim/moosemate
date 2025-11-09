package controller;

import dto.ApiResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.ApiClient;
import util.ValidationUtils;

public class SignUpController extends BaseController {
    
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    private final ApiClient apiClient;

    public SignUpController() {
        this.apiClient = ApiClient.getInstance();
    }

    @FXML
    private void handleSignUpButton(ActionEvent event) {
        clearError(); 
        
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        
        try {
            if (ValidationUtils.anyNullOrEmpty(username, email, password)) {
                showError("All fields are required");
                return;
            }
            
            ApiResponse<?> response = apiClient.signUp(username, email, password);
            
            if (response.isSuccess()) {
                navigateToOtherPageWithSuccess(event, "/fxml/loginpage.fxml", "Login", 
                        "Account created successfully!");
            } else {
                showError(response.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error handling signup: " + e.getMessage());
            showError("Unable to connect to server. Please ensure the REST API is running.");
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
