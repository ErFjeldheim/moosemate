package moosemate.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import moosemate.services.LoginService;

public class LoginController extends BaseController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

// navigates to sign up page 
    @FXML
    private void handleSignUpButton(ActionEvent event){
        try {
            navigateToOtherPage(event, "/fxml/signuppage.fxml", "Sign Up");
        } catch (Exception e) {
            System.err.println("Error loading sign up page: " + e.getMessage());
        }
    }


// handles login button and navigates to home page if user exists
    @FXML 
    private void handleLoginButton(ActionEvent event) {
        try {
            LoginService loginService = new LoginService();
            if (loginService.loginUser(usernameField.getText(), passwordField.getText())) {
                navigateToOtherPage(event, "/fxml/homepage.fxml", "Home");
            }
        } catch (Exception e) {
            System.err.println("Error handling login: " + e.getMessage());
        }
    }
}