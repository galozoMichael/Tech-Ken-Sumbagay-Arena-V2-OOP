package com.techken.ui;

import com.techken.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class SettingsController {

    @FXML private Button saveBtn;
    @FXML private Button resetBtn;

    @FXML private Slider musicSlider;
    @FXML private Label musicValueLabel;

    @FXML private CheckBox fullscreenCheckBox;
    @FXML private ComboBox<String> resolutionComboBox;

    @FXML
    public void initialize() {
        // Populate Resolution options
        resolutionComboBox.getItems().addAll(
                "1280 x 720",
                "1920 x 1080",
                "2560 x 1440"
        );
        resolutionComboBox.setValue("1280 x 720");

        // Sync music slider to current MediaPlayer volume on open
        if (MainApp.mediaPlayer != null) {
            double currentVolume = MainApp.mediaPlayer.getVolume() * 100;
            musicSlider.setValue(currentVolume);
            musicValueLabel.setText(String.format("%.0f%%", (double) musicSlider.getValue()));
        } else {
            musicSlider.setValue(80);
            musicValueLabel.setText("80%");
        }

        // Live update label and volume as slider moves
        musicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            musicValueLabel.setText(String.format("%.0f%%", newVal.doubleValue()));
            if (MainApp.mediaPlayer != null) {
                MainApp.mediaPlayer.setVolume(newVal.doubleValue() / 100.0);
            }
        });
    }

    @FXML
    private void onMusicVolumeChanged() {
        double value = musicSlider.getValue();
        musicValueLabel.setText(String.format("%.0f%%", value));
        if (MainApp.mediaPlayer != null) {
            MainApp.mediaPlayer.setVolume(value / 100.0);
        }
    }

    @FXML
    private void onFullscreenToggled() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.setFullScreen(fullscreenCheckBox.isSelected());
    }

    @FXML
    private void onResolutionChanged() {
        String selected = resolutionComboBox.getValue();
        System.out.println("Resolution changed to: " + selected);
    }

    @FXML
    private void onSave() {
        System.out.println("Settings saved.");
    }

    @FXML
    private void onReset() {
        musicSlider.setValue(80);
        fullscreenCheckBox.setSelected(false);
        resolutionComboBox.setValue("1280 x 720");

        if (MainApp.mediaPlayer != null) {
            MainApp.mediaPlayer.setVolume(0.8);
        }

        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.setFullScreen(false);

        System.out.println("Settings reset to defaults.");
    }

    @FXML
    private void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
