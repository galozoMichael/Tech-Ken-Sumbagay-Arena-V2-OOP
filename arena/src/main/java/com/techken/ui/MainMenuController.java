package com.techken.ui;

import com.techken.MainApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML private Button startBtn;

    @FXML
    private void Play() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CharacterSelect.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage) startBtn.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void Credits() {
        System.out.println("Loading Credits...");

    }

    @FXML
    private void Settings() {
        System.out.println("Opening Settings panel...");
    }

    @FXML
    private void Profile() {
        System.out.println("Loading Player Profile... (Fetching ELO, Wins, and Losses!)");

    }

    @FXML private void Exit() { Platform.exit(); }

    @FXML public void initialize() {
        MainApp.playMusic("menutestaudio.mp3");
    }

}