package controller;

import dto.ApiResponse;
import dto.MoosageDto;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.ApiClient;
import util.ValidationUtils;

import java.io.IOException;
import java.util.function.Consumer;

// Custom ListCell for displaying MoosageDto objects in a ListView.
// Uses moosagecell.fxml for layout.
 
public class MoosageListCell extends ListCell<MoosageDto> {
    
    @FXML
    private Label usernameLabel;
    
    @FXML
    private Label dateLabel;
    
    @FXML
    private Label editedLabel;
    
    @FXML
    private Text contentText;
    
    @FXML
    private Button likeButton;
    
    @FXML
    private MenuButton menuButton;
    
    @FXML
    private MenuItem editMenuItem;
    
    @FXML
    private MenuItem deleteMenuItem;
    
    private VBox cellContent;
    
    // Callback to notify parent when a moosage is deleted
    private Consumer<MoosageDto> onDeleteCallback;
    
    public void setOnDeleteCallback(Consumer<MoosageDto> callback) {
        this.onDeleteCallback = callback;
    }
    
    @Override
    protected void updateItem(MoosageDto moosage, boolean empty) {
        super.updateItem(moosage, empty);
        
        if (empty || moosage == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Load FXML each time (or reuse if already loaded for this cell)
            if (cellContent == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/moosagecell.fxml"));
                loader.setController(this);
                
                try {
                    cellContent = loader.load();
                } catch (IOException e) {
                    System.err.println("Error loading moosagecell.fxml: " + e.getMessage());
                    return;
                }
            }
            
            usernameLabel.setText(moosage.getAuthorUsername());
            
            contentText.setText(moosage.getContent());
            
            dateLabel.setText(formatTimestamp(moosage.getTime()));
            
            // Show "(edited)" label if moosage has been edited
            if (moosage.isEdited()) {
                editedLabel.setVisible(true);
                editedLabel.setManaged(true);
            } else {
                editedLabel.setVisible(false);
                editedLabel.setManaged(false);
            }
            
            // Update like button text and style based on current user's like status
            updateLikeButton(moosage);
            likeButton.setOnAction(event -> handleLike(moosage));
            
            // Only show menu button if current user is the author
            String currentUserId = service.SessionManager.getInstance().getUserId();
            if (currentUserId != null && currentUserId.equals(moosage.getAuthorId())) {
                menuButton.setVisible(true);
                menuButton.setManaged(true);
                editMenuItem.setOnAction(event -> handleEdit(moosage));
                deleteMenuItem.setOnAction(event -> handleDelete(moosage));
            } else {
                menuButton.setVisible(false);
                menuButton.setManaged(false);
            }
            
            setGraphic(cellContent);
        }
    }
    
    // Formats the timestamp for display
    private String formatTimestamp(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.Duration duration = java.time.Duration.between(dateTime, now);
        
        long hours = duration.toHours();
        
        // If less than 24 hours ago, show "X hours ago"
        if (hours < 24) {
            if (hours == 0) {
                long minutes = duration.toMinutes();
                if (minutes == 0) {
                    return "Right now";
                } else if (minutes == 1) {
                    return "1 minute ago";
                } else {
                    return minutes + " minutes ago";
                }
            } else if (hours == 1) {
                return "1 hour ago";
            } else {
                return hours + " hours ago";
            }
        }
        
        // Otherwise show full date: "Jan 15, 2025 14:30"
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
    }
    
    // Updates like button text and style based on whether current user has liked the moosage
    private void updateLikeButton(MoosageDto moosage) {
        String currentUserId = service.SessionManager.getInstance().getUserId();
        boolean isLiked = currentUserId != null && moosage.isLikedBy(currentUserId);
        int likeCount = moosage.getLikeCount();
        
        // Update button text with like count
        likeButton.setText("♥ " + likeCount);
        
        // Add or remove "liked" style class
        if (isLiked) {
            if (!likeButton.getStyleClass().contains("liked")) {
                likeButton.getStyleClass().add("liked");
            }
        } else {
            likeButton.getStyleClass().remove("liked");
        }
    }
    
    // Handles like button click
    private void handleLike(MoosageDto moosage) {
        // Call backend to toggle like
        new Thread(() -> {
            try {
                ApiClient apiClient = new ApiClient();
                ApiResponse<MoosageDto> response = apiClient.toggleLike(moosage.getId());
                
                if (response.isSuccess() && response.getData() != null) {
                    MoosageDto updatedMoosage = response.getData();
                    
                    // Update UI on JavaFX thread
                    Platform.runLater(() -> {
                        // Update the moosage data in the list
                        moosage.setLikedByUserIds(updatedMoosage.getLikedByUserIds());
                        
                        // Update the like button display
                        updateLikeButton(moosage);
                        System.out.println("Like toggled successfully for moosage: " + moosage.getId());
                    });
                } else {
                    System.err.println("Failed to toggle like: " + response.getMessage());
                }
                
            } catch (Exception e) {
                System.err.println("Error toggling like: " + e.getMessage());
            }
        }).start();
    }
    
    // Handles edit button click
    private void handleEdit(MoosageDto moosage) {
        try {
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editmoosage.fxml"));
            Parent root = loader.load();

            // Get the controller and set initial content
            EditMoosageController dialogController = loader.getController();
            dialogController.setContent(moosage.getContent());

            // Create the stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Moosage");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            // Show the dialog and wait for it to close
            dialogStage.showAndWait();

            // Get the result
            String newContent = dialogController.getResult();
            if (!ValidationUtils.isNullOrEmpty(newContent) && !newContent.equals(moosage.getContent())) {
                // Call backend to update moosage
                new Thread(() -> {
                    try {
                        ApiClient apiClient = new ApiClient();
                        ApiResponse<MoosageDto> response = apiClient.updateMoosage(moosage.getId(), newContent.trim());
                        
                        if (response.isSuccess() && response.getData() != null) {
                            MoosageDto updatedMoosage = response.getData();
                            
                            // Update UI on JavaFX thread
                            Platform.runLater(() -> {
                                contentText.setText(updatedMoosage.getContent());
                                // Show "(edited)" label
                                editedLabel.setVisible(true);
                                editedLabel.setManaged(true);
                            });
                        } else {
                            System.err.println("Failed to update moosage: " + response.getMessage());
                        }
                        
                    } catch (Exception e) {
                        System.err.println("Error updating moosage: " + e.getMessage());
                    }
                }).start();
            }
        } catch (Exception e) {
            System.err.println("Failed to open edit dialog: " + e.getMessage());
        }
    }
    
    // Handles delete button click
    private void handleDelete(MoosageDto moosage) {
        // Call backend to delete moosage
        new Thread(() -> {
            try {
                ApiClient apiClient = new ApiClient();
                ApiResponse<Void> response = apiClient.deleteMoosage(moosage.getId());
                
                if (response.isSuccess()) {
                    // Notify parent to remove from list
                    Platform.runLater(() -> {
                        if (onDeleteCallback != null) {
                            onDeleteCallback.accept(moosage);
                        }
                    });
                } else {
                    System.err.println("Failed to delete moosage: " + response.getMessage());
                }
                
            } catch (Exception e) {
                System.err.println("Error deleting moosage: " + e.getMessage());
            }
        }).start();
    }
}
