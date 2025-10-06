package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for the splash screen that shows while the app loads.
 */
public class LoadingScreenController {

    @FXML
    private StackPane rootPane;
    
    @FXML
    private Label loadingLabel;
    
    private Timeline dotAnimation;

    @FXML
    public void initialize() {
        // Start the animated dots
        startLoadingAnimation();
        
        // Automatically transition to the homepage after 3 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            // Stop the animation
            if (dotAnimation != null) {
                dotAnimation.stop();
            }
            
            Platform.runLater(() -> {
                try {
                    loadLoginPage();
                } catch (Exception e) {
                    System.err.println("Error loading login page: " + e.getMessage());
                }
            });
        });
        pause.play();
    }
    
    private void startLoadingAnimation() {
        final String[] dots = {"", ".", "..", "..."};
        final int[] index = {0};

        // Create a timeline that updates every 300ms
        dotAnimation = new Timeline(new KeyFrame(Duration.millis(300), event -> {
            loadingLabel.setText("Loading" + dots[index[0]]);
            index[0] = (index[0] + 1) % dots.length;
        }));
        
        dotAnimation.setCycleCount(Animation.INDEFINITE);
        dotAnimation.play();
    }

    private void loadLoginPage() throws Exception {
        // Load the homepage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homepage.fxml"));
        Scene homeScene = new Scene(loader.load());
        
        // Get the current stage from the root pane
        Stage stage = (Stage) rootPane.getScene().getWindow();
        
        // Set the new scene
        stage.setScene(homeScene);
        stage.setTitle("MooseMate - Home");
    }
}
