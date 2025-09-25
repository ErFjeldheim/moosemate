package moosemate.ui;

import javafx.fxml.FXML;


import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import moosemate.services.SignUpService;

public class SignUpController extends BaseController {
    
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private void handleSignUpButton(ActionEvent event) {
        // Clear any previously displayed error messages
        clearError(); 
        
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        
        try {
            SignUpService signUpService = new SignUpService();
            boolean signUpSuccess = signUpService.signUpUser(username, email, password);

            if (signUpSuccess) {
                System.out.println("Sign up completed!");
                navigateToOtherPageWithSuccess(event, "/fxml/loginpage.fxml", "Login", "Sign up successful!");
            } else {
                // Check specific reasons for failure to sign up
                if (signUpService.userExists(username)) {
                    showError("Username already exists");
                } else if (signUpService.emailExists(email)) {
                    showError("Email already registered");
                } else {
                    showError("Sign up failed. Please try again.");
                }
            }
            
        } catch (IllegalArgumentException e) {
            // This catches validation errors from User class constructor
            showError(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error handling signup: " + e.getMessage());
            showError("An unexpected error occurred. Please try again.");
        }
    }
    
    @FXML
    private void handleBackToLoginButton(ActionEvent event) {
        try {
            navigateToOtherPage(event, "/fxml/loginpage.fxml", "Login");
        } catch (Exception e) {
            System.err.println("Error navigating back to login page: " + e.getMessage());
        }
    }
}
