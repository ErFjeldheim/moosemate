package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import service.ApiClient;
import service.SessionManager;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadCustomFonts();
        
        // Set application icon - using logo as fallback if icon doesn't exist
        try {
            var iconStream = getClass().getResourceAsStream("/images/moosemate_logo.png");
            if (iconStream != null) {
                primaryStage.getIcons().add(new Image(iconStream));
            }
        } catch (Exception e) {
            // Icon loading failed
        }
        
        primaryStage.setOnCloseRequest(event -> {
            handleApplicationClose();
        });
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/loginpage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("MooseMate");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void loadCustomFonts() {
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/Chewy-Regular.ttf"), 12);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Chewy-Bold.ttf"), 12);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Medium.ttf"), 12);
            
        } catch (Exception e) {
            System.err.println("Failed to load custom fonts: " + e.getMessage());
        }
    }
    
    private void handleApplicationClose() {
        String sessionToken = SessionManager.getInstance().getSessionToken();
        
        if (sessionToken != null) {
            try {
                ApiClient apiClient = ApiClient.getInstance();
                apiClient.logout(sessionToken);
                SessionManager.getInstance().logout();
            } catch (Exception e) {
                System.err.println("Failed to logout on application close: " + e.getMessage());
                // Clear local session even if server logout fails
                SessionManager.getInstance().logout();
            }
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
