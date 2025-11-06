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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoadingScreenController {

    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private Label loadingLabel;
    
    private Timeline dotAnimation;

    @FXML
    public void initialize() {
        startLoadingAnimation();
        
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            if (dotAnimation != null) {
                dotAnimation.stop();
            }
            
            Platform.runLater(() -> {
                try {
                    loadHomePage();
                } catch (Exception e) {
                    System.err.println("Error loading Home Page: " + e.getMessage());
                }
            });
        });
        pause.play();
    }
    
    private void startLoadingAnimation() {
        final String[] dots = {"", ".", "..", "..."};
        final int[] index = {0};

        dotAnimation = new Timeline(new KeyFrame(Duration.millis(300), event -> {
            loadingLabel.setText("Luring Moosages" + dots[index[0]]);
            index[0] = (index[0] + 1) % dots.length;
        }));
        
        dotAnimation.setCycleCount(Animation.INDEFINITE);
        dotAnimation.play();
    }

    private void loadHomePage() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homepage.fxml"));
        Scene homeScene = new Scene(loader.load());
        
        Stage stage = (Stage) rootPane.getScene().getWindow();
        
        stage.setScene(homeScene);
        stage.setTitle("MooseMate - Home");
    }
}
