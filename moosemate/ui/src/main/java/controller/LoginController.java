package controller;

import dto.ApiResponse;
import dto.LoginResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.ApiClient;
import service.SessionManager;
import util.ValidationUtils;

public class LoginController extends BaseController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    private final ApiClient apiClient;

    public LoginController() {
        this.apiClient = new ApiClient();
    }

    @FXML
    private void handleSignUpButton(ActionEvent event) {
        try {
            navigateToOtherPage(event, "/fxml/signuppage.fxml", "Sign Up");
        } catch (Exception e) {
            System.err.println("Error loading sign up page: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        clearError();

        if (ValidationUtils.anyNullOrEmpty(username, password)) {
            showError("Username and password are required");
            return;
        }

        try {
            ApiResponse<LoginResponse> response = apiClient.login(username, password);

            if (response.isSuccess()) {
                // store session
                SessionManager.getInstance().login(response.getData());
                navigateToOtherPage(event, "/fxml/loadingscreen.fxml", "Loading...");
            } else {
                showError(response.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error handling login: " + e.getMessage());
            showError("Unable to connect to server. Please ensure the REST API is running.");
        }
    }
}
