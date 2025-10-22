package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import service.SessionManager;
import service.ApiClient;

public class HomePageController extends BaseController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        // Get the current username from SessionManager
        String username = SessionManager.getInstance().getUsername();
        
        // Set the welcome message
        if (username != null && !username.isEmpty()) {
            welcomeLabel.setText("Welcome " + username);
        } else {
            welcomeLabel.setText("Welcome");
        }
    }

    @FXML
    private void handleLogoutButton(ActionEvent event) {
        try {
            // Get session token from SessionManager
            String sessionToken = SessionManager.getInstance().getSessionToken();
            
            if (sessionToken != null) {
                // Call logout endpoint on server
                ApiClient apiClient = new ApiClient();
                apiClient.logout(sessionToken);
                System.out.println("Logout request sent to server");
            }
            
            // Clear local session data
            SessionManager.getInstance().logout();
            System.out.println("Local session cleared");
            
            // Navigate to login page
            navigateToOtherPage(event, "/fxml/loginpage.fxml", "Login");
        } catch (Exception e) {
            System.err.println("Error occurred when trying to logout: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
