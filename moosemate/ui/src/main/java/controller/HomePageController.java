package controller;

import dto.ApiResponse;
import dto.MoosageDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.ApiClient;
import service.SessionManager;
import util.ValidationUtils;

import java.util.List;

public class HomePageController extends BaseController {

    @FXML
    private Label loggedInLabel;

    @FXML
    private javafx.scene.image.ImageView logoutIcon;

    @FXML
    private javafx.scene.control.ListView<MoosageDto> moosageList;

    @FXML
    private javafx.scene.control.TextArea postTextArea;

    @FXML
    private javafx.scene.control.Button postButton;

    @FXML
    private Label postCharCountLabel;

    private ObservableList<MoosageDto> moosages;
    private static final int MAX_CHARS = 280;

    @FXML
    public void initialize() {
        // Get the current username from SessionManager
        String username = SessionManager.getInstance().getUsername();

        // Set personalized prompt text
        if (username != null && !username.isEmpty()) {
            postTextArea.setPromptText("What's between your antlers, " + username + "?");
        }

        // Add listener to update character count
        postTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            updateCharCount(newValue);
        });

        // Limit text length
        postTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > MAX_CHARS) {
                postTextArea.setText(oldValue);
            }
        });

        // Load moosages from backend
        loadMoosages();
    }

    /**
     * Updates the character count label for post text area
     */
    private void updateCharCount(String text) {
        int length = text != null ? text.length() : 0;
        postCharCountLabel.setText(length + "/" + MAX_CHARS);
        
        // Change color if approaching limit
        if (length > MAX_CHARS * 0.9) {
            postCharCountLabel.setStyle("-fx-text-fill: #c94a4a;");
        } else {
            postCharCountLabel.setStyle("-fx-text-fill: #3d3d3dda;");
        }
    }

    // Loads moosages from the backend API and displays them in the list view
    private void loadMoosages() {
        try {
            ApiClient apiClient = new ApiClient();
            ApiResponse<List<MoosageDto>> response = apiClient.getMoosages();
            
            if (response.isSuccess() && response.getData() != null) {
                moosages = FXCollections.observableArrayList(response.getData());
                moosageList.setItems(moosages);
                
                // Set custom cell factory for better display
                moosageList.setCellFactory(param -> {
                    MoosageListCell cell = new MoosageListCell();
                    cell.setOnDeleteCallback(this::handleMoosageDeleted);
                    return cell;
                });
            } else {
                System.err.println("Failed to load moosages: " + response.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("Error loading moosages: " + e.getMessage());
        }
    }

    @FXML
    private void handlePostButton(ActionEvent event) {
        String content = postTextArea.getText();
        if (ValidationUtils.isNullOrEmpty(content)) {
            return;
        }

        // Disable UI while posting
        postButton.setDisable(true);

        new Thread(() -> {
            try {
                ApiClient apiClient = new ApiClient();
                ApiResponse<MoosageDto> response = apiClient.postMoosage(content.trim());

                if (response != null && response.isSuccess() && response.getData() != null) {
                    MoosageDto created = response.getData();
                    javafx.application.Platform.runLater(() -> {
                        if (moosages == null) {
                            moosages = FXCollections.observableArrayList();
                            moosageList.setItems(moosages);
                            moosageList.setCellFactory(param -> {
                                MoosageListCell cell = new MoosageListCell();
                                cell.setOnDeleteCallback(this::handleMoosageDeleted);
                                return cell;
                            });
                        }
                        // Add new moosage at top
                        moosages.add(0, created);
                        postTextArea.clear();
                    });
                } else {
                    System.err.println("Failed to create moosage: " 
                            + (response != null ? response.getMessage() : "null response"));
                }
            } catch (Exception e) {
                System.err.println("Error posting moosage: " + e.getMessage());
            } finally {
                javafx.application.Platform.runLater(() -> postButton.setDisable(false));
            }
        }).start();
    }

    // Callback when a moosage is deleted
    private void handleMoosageDeleted(MoosageDto deletedMoosage) {
        if (moosages != null) {
            moosages.remove(deletedMoosage);
        }
    }

    @FXML
    private void handleLogoutButton(MouseEvent event) {
        try {
            // Get session token from SessionManager
            String sessionToken = SessionManager.getInstance().getSessionToken();
            
            if (sessionToken != null) {
                // Call logout endpoint on server
                ApiClient apiClient = new ApiClient();
                apiClient.logout(sessionToken);
            }
            
            // Clear local session data
            SessionManager.getInstance().logout();
            
            // Load and navigate to login page
            java.net.URL resourceUrl = getClass().getResource("/fxml/loginpage.fxml");
            if (resourceUrl == null) {
                throw new Exception("FXML resource not found: /fxml/loginpage.fxml");
            }
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(resourceUrl);
            javafx.scene.Parent root = loader.load();
            
            // Get stage and set new scene
            Stage stage = (Stage) logoutIcon.getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
            
        } catch (Exception e) {
            System.err.println("Error occurred when trying to logout: " + e.getMessage());
        }
    }
    
}
