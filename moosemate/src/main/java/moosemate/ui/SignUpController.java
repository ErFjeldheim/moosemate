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
        try {
            //calls the sign up service to write to file and run further logic implemented in the service
            SignUpService signUpService = new SignUpService();
            if (signUpService.signUpUser(usernameField.getText(), emailField.getText(), passwordField.getText())) {
                // Navigate back to login page or to a success page
                System.out.println("Sign up completed!");
                navigateToOtherPage(event, "/fxml/loginpage.fxml", "Login");
            }
        } catch (Exception e) {
            System.err.println("Error handling signup: " + e.getMessage());
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
