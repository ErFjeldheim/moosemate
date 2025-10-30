package controller;

import dto.ApiResponse;
import dto.MoosageDto;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import service.ApiClient;

import java.io.IOException;
import java.util.Optional;
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
    private Label likeCountLabel;
    
    @FXML
    private Button editButton;
    
    @FXML
    private Button deleteButton;
    
    private VBox cellContent;
    
    // Callback to notify parent when a moosage is deleted
    private Consumer<MoosageDto> onDeleteCallback;
    
    public void setOnDeleteCallback(Consumer<MoosageDto> callback) {
        this.onDeleteCallback = callback;
    }
    
    @Override
    protected void updateItem(MoosageDto moosage, boolean empty) {
        super.updateItem(moosage, empty);
        
        System.out.println("MoosageListCell.updateItem called - empty: " + empty + ", moosage: " + (moosage != null ? moosage.getContent() : "null"));
        
        if (empty || moosage == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Load FXML each time (or reuse if already loaded for this cell)
            if (cellContent == null) {
                System.out.println("Loading FXML for cell...");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/moosagecell.fxml"));
                loader.setController(this);
                
                try {
                    cellContent = loader.load();
                    System.out.println("FXML loaded successfully");
                } catch (IOException e) {
                    System.err.println("Error loading moosagecell.fxml: " + e.getMessage());
                    e.printStackTrace();
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
            
            // Display like count String.valueOf(updatedMoosage.getLikeCount())
            likeCountLabel.setText(String.valueOf(moosage.getLikeCount()));
            likeButton.setOnAction(event -> handleLike(moosage));
            
            // Only show edit and delete buttons if current user is the author
            String currentUserId = service.SessionManager.getInstance().getUserId();
            if (currentUserId != null && currentUserId.equals(moosage.getAuthorId())) {
                editButton.setVisible(true);
                editButton.setManaged(true);
                editButton.setOnAction(event -> handleEdit(moosage));
                
                deleteButton.setVisible(true);
                deleteButton.setManaged(true);
                deleteButton.setOnAction(event -> handleDelete(moosage));
            } else {
                editButton.setVisible(false);
                editButton.setManaged(false);
                
                deleteButton.setVisible(false);
                deleteButton.setManaged(false);
            }
            
            System.out.println("Setting graphic for moosage: " + moosage.getContent().substring(0, Math.min(20, moosage.getContent().length())));
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
                        likeCountLabel.setText(String.valueOf(updatedMoosage.getLikeCount()));
                        System.out.println("Like toggled successfully for moosage: " + moosage.getId());
                    });
                } else {
                    System.err.println("Failed to toggle like: " + response.getMessage());
                }
                
            } catch (Exception e) {
                System.err.println("Error toggling like: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
    
    // Handles edit button click
    private void handleEdit(MoosageDto moosage) {
        // Show dialog to edit content
        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog(moosage.getContent());
            dialog.setTitle("Edit Moosage");
            dialog.setHeaderText("Edit your moosage");
            dialog.setContentText("New content:");
            
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newContent -> {
                if (newContent != null && !newContent.trim().isEmpty()) {
                    // Call backend to update moosage
                    new Thread(() -> {
                        try {
                            ApiClient apiClient = new ApiClient();
                            System.out.println("Attempting to update moosage ID: " + moosage.getId() + " with content: " + newContent.trim());
                            ApiResponse<MoosageDto> response = apiClient.updateMoosage(moosage.getId(), newContent.trim());
                            
                            System.out.println("Update response - Success: " + response.isSuccess() + ", Message: " + response.getMessage() + ", Data: " + (response.getData() != null ? "present" : "null"));
                            
                            if (response.isSuccess() && response.getData() != null) {
                                MoosageDto updatedMoosage = response.getData();
                                
                                // Update UI on JavaFX thread
                                Platform.runLater(() -> {
                                    contentText.setText(updatedMoosage.getContent());
                                    // Show "(edited)" label
                                    editedLabel.setVisible(true);
                                    editedLabel.setManaged(true);
                                    System.out.println("Moosage updated successfully: " + moosage.getId());
                                });
                            } else {
                                System.err.println("Failed to update moosage: " + response.getMessage());
                            }
                            
                        } catch (Exception e) {
                            System.err.println("Error updating moosage: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }).start();
                }
            });
        });
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
                        System.out.println("Moosage deleted successfully: " + moosage.getId());
                    });
                } else {
                    System.err.println("Failed to delete moosage: " + response.getMessage());
                }
                
            } catch (Exception e) {
                System.err.println("Error deleting moosage: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
