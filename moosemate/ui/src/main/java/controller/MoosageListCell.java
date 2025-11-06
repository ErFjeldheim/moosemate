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
            
            if (moosage.isEdited()) {
                editedLabel.setVisible(true);
                editedLabel.setManaged(true);
            } else {
                editedLabel.setVisible(false);
                editedLabel.setManaged(false);
            }
            
            updateLikeButton(moosage);
            likeButton.setOnAction(event -> handleLike(moosage));
            
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
    
    private String formatTimestamp(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.Duration duration = java.time.Duration.between(dateTime, now);
        
        long hours = duration.toHours();
        
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
        
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
    }
    
    private void updateLikeButton(MoosageDto moosage) {
        String currentUserId = service.SessionManager.getInstance().getUserId();
        boolean isLiked = currentUserId != null && moosage.isLikedBy(currentUserId);
        int likeCount = moosage.getLikeCount();
        
        likeButton.setText("♥ " + likeCount);
        
        if (isLiked) {
            if (!likeButton.getStyleClass().contains("liked")) {
                likeButton.getStyleClass().add("liked");
            }
        } else {
            likeButton.getStyleClass().remove("liked");
        }
    }
    
    private void handleLike(MoosageDto moosage) {
        new Thread(() -> {
            try {
                ApiClient apiClient = new ApiClient();
                ApiResponse<MoosageDto> response = apiClient.toggleLike(moosage.getId());
                
                if (response.isSuccess() && response.getData() != null) {
                    MoosageDto updatedMoosage = response.getData();
                    
                    Platform.runLater(() -> {
                        moosage.setLikedByUserIds(updatedMoosage.getLikedByUserIds());
                        
                        updateLikeButton(moosage);
                    });
                } else {
                    System.err.println("Failed to toggle like: " + response.getMessage());
                }
            } catch (Exception e) {
                System.err.println("Error toggling like: " + e.getMessage());
            }
        }).start();
    }
    
    private void handleEdit(MoosageDto moosage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editmoosage.fxml"));
            Parent root = loader.load();

            EditMoosageController dialogController = loader.getController();
            dialogController.setContent(moosage.getContent());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Moosage");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            dialogStage.showAndWait();

            String newContent = dialogController.getResult();
            if (!ValidationUtils.isNullOrEmpty(newContent) && !newContent.equals(moosage.getContent())) {
                new Thread(() -> {
                    try {
                        ApiClient apiClient = new ApiClient();
                        ApiResponse<MoosageDto> response = apiClient.updateMoosage(moosage.getId(), newContent.trim());
                        
                        if (response.isSuccess() && response.getData() != null) {
                            MoosageDto updatedMoosage = response.getData();
                            
                            Platform.runLater(() -> {
                                contentText.setText(updatedMoosage.getContent());
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
    
    private void handleDelete(MoosageDto moosage) {
        new Thread(() -> {
            try {
                ApiClient apiClient = new ApiClient();
                ApiResponse<Void> response = apiClient.deleteMoosage(moosage.getId());
                
                if (response.isSuccess()) {
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
