package moosemate.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BaseController {
    
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
}
