package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class EditMoosageController {
    
    @FXML
    private TextArea contentTextArea;

    @FXML
    private Label charCountLabel;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    private String result = null;
    private static final int MAX_CHARS = 280;

    @FXML
    private void initialize() {
        // Add listener to update character count
        contentTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            updateCharCount(newValue);
        });

        contentTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > MAX_CHARS) {
                contentTextArea.setText(oldValue);
            }
        });
    }

    public void setContent(String content) {
        contentTextArea.setText(content);
        updateCharCount(content);
    }

    public String getResult() {
        return result;
    }

    private void updateCharCount(String text) {
        int length = text != null ? text.length() : 0;
        charCountLabel.setText(length + "/" + MAX_CHARS);
        
        if (length > MAX_CHARS * 0.9) {
            charCountLabel.setStyle("-fx-text-fill: #c94a4a;");
        } else {
            charCountLabel.setStyle("-fx-text-fill: #3d3d3dda;");
        }
    }

    @FXML
    private void handleSave() {
        result = contentTextArea.getText().trim();
        closeDialog();
    }

    @FXML
    private void handleCancel() {
        result = null;
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
