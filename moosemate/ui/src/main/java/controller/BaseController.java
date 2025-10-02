package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class BaseController {

    @FXML
    private Label errorLabel; 
    
    protected void navigateToOtherPage(ActionEvent event, String fxmlPath, String title) throws IOException {
        // Loads the FXML file it want to navigate to
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        
        // Get the current stage 
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Opens the new fxml file in the same window/tab
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    // Overloaded method to navigate and show success message on target page
    protected void navigateToOtherPageWithSuccess(ActionEvent event, String fxmlPath, String title, String successMessage) throws IOException {
        // Loads the FXML file it want to navigate to
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        
        // Get the controller of the target page to set success message
        BaseController targetController = loader.getController();
        if (targetController != null) {
            targetController.showSuccess(successMessage);
        }
        
        // Get the current stage 
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Opens the new fxml file in the same window/tab
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    //Displays error message to user
    protected void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
            errorLabel.getStyleClass().clear();
            errorLabel.getStyleClass().add("error-label");
        }
    }

    //Displays success message to user
    protected void showSuccess(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
            errorLabel.getStyleClass().clear();
            errorLabel.getStyleClass().add("success-label");
        }
    }

    //Clears the error message from the page
    protected void clearError() {
        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
            errorLabel.getStyleClass().clear();
        }
    }
}
