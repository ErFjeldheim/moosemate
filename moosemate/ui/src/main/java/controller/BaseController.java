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
        java.net.URL resourceUrl = BaseController.class.getResource(fxmlPath);
        if (resourceUrl == null) {
            throw new IOException("FXML resource not found: " + fxmlPath);
        }
        FXMLLoader loader = new FXMLLoader(resourceUrl);
        Parent root = loader.load();
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    protected void navigateToOtherPageWithSuccess(ActionEvent event, String fxmlPath,
            String title, String successMessage) throws IOException {
        java.net.URL resourceUrl = BaseController.class.getResource(fxmlPath);
        if (resourceUrl == null) {
            throw new IOException("FXML resource not found: " + fxmlPath);
        }
        FXMLLoader loader = new FXMLLoader(resourceUrl);
        Parent root = loader.load();
        
        BaseController targetController = loader.getController();
        if (targetController != null) {
            targetController.showSuccess(successMessage);
        }
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    protected void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
            errorLabel.getStyleClass().clear();
            errorLabel.getStyleClass().add("error-label");
        }
    }

    protected void showSuccess(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
            errorLabel.getStyleClass().clear();
            errorLabel.getStyleClass().add("success-label");
        }
    }

    protected void clearError() {
        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
            errorLabel.getStyleClass().clear();
        }
    }
}
