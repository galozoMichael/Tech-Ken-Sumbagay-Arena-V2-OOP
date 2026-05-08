package com.techken.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ProfileController {

    @FXML private Button backBtn;
    @FXML private Button btnEditProfile;

    @FXML private Label lblPlayerName;
    @FXML private Label lblUsername;
    @FXML private Label lblStats;

    @FXML
    public void initialize() {
        // TODO: Replace these with real player data later
        lblPlayerName.setText("PLAYER ONE");
        lblUsername.setText("@player1");
        lblStats.setText("WINS: 0  |  LOSSES: 0  |  WIN RATE: 0%");
    }

    @FXML
    private void backToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editProfile() {
        System.out.println("Edit Profile clicked...");
        // TODO: Add edit profile logic here
    }
}