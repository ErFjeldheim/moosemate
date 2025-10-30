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
        // Load custom fonts from resources
        loadCustomFonts();
        
        // Set the application icon (appears in window title bar and taskbar)
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Moosemate-logo-v2.png")));
        
        // Handle window close - logout user if session exists
        primaryStage.setOnCloseRequest(event -> {
            handleApplicationClose();
        });
        
        // Load the login page
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/loginpage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("MooseMate");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void loadCustomFonts() {
        try {
            // Load all Optima font variants
            Font.loadFont(getClass().getResourceAsStream("/fonts/Optima-Regular.ttf"), 12);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Optima-Bold.ttf"), 12);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Optima-Italic.ttf"), 12);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Optima-BoldItalic.ttf"), 12);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Optima-ExtraBlack.ttf"), 12);
            
            System.out.println("Custom fonts loaded successfully");
        } catch (Exception e) {
            System.err.println("Failed to load custom fonts: " + e.getMessage());
        }
    }
    
    // Handles application close event by logging out user if session exists.
    // This ensures session is terminated on server when user closes the application.
    private void handleApplicationClose() {
        String sessionToken = SessionManager.getInstance().getSessionToken();
        
        if (sessionToken != null) {
            try {
                System.out.println("Application closing - logging out user");
                ApiClient apiClient = new ApiClient();
                apiClient.logout(sessionToken);
                SessionManager.getInstance().logout();
                System.out.println("User logged out successfully on application close");
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
