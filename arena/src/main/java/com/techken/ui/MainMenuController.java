package com.techken.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;



public class MainMenuController {

    @FXML private Button startBtn;

    @FXML private void Play() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CharacterSelect.fxml"));
            Scene scene = new Scene(loader.load(), 1024, 640);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage stage = (Stage) startBtn.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void CharacterSelect() { System.out.println("Character Select"); }

    @FXML private void HowToPlay() { System.out.println("How To Play"); }

    @FXML private void Credits() { System.out.println("Credits"); }

    @FXML private void Settings() { System.out.println("Settings"); }

    @FXML private void Exit() { Platform.exit(); }
}