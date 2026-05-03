package com.techken;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MainApp extends Application {

    public static MediaPlayer mediaPlayer;
    private static String currentTrack = "";
    public static void playMusic(String filename) {
        if (currentTrack.equals(filename)) return;

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        currentTrack = filename;
        String path = MainApp.class.getResource("/audio/" + filename).toExternalForm();
        Media media = new Media(path);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(loader.load(), 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setTitle("Tech-Ken: Sumbagay Arena");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo_transparent.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}