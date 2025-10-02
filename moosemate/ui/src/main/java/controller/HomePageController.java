package controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class HomePageController extends BaseController {

    @FXML
    private void handleLogoutButton(ActionEvent event){
        try {
            navigateToOtherPage(event, "/fxml/loginpage.fxml", "Login");
        } catch (Exception e) {
            System.err.println("Error occurred when trying to log out: " + e.getMessage());
        }
    }
    
}
