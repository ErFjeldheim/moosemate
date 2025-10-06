package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HomePageController extends BaseController {

    @FXML
    private void handleLogoutButton(ActionEvent event) {
        try {
            navigateToOtherPage(event, "/fxml/loginpage.fxml", "Login");
        } catch (Exception e) {
            System.err.println("Error occurred when trying to log out: " + e.getMessage());
        }
    }
    
}
