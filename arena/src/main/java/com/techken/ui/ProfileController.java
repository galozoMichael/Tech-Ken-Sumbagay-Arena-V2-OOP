package com.techken.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.util.Properties;

public class ProfileController {

    // Save file location
    private static final String SAVE_FILE = "profile.properties";

    // Data
    static String playerName = "Player1";
    static int wins = 0;
    static int losses = 0;

    @FXML private Button backBtn;
    @FXML private Button btnResetProfile;
    @FXML private Label lblPlayerName;
    @FXML private TextField txtEditName;
    @FXML private Label lblWins;
    @FXML private Label lblLosses;
    @FXML private Label lblWinRate;
    @FXML private Label lblClickHint;
    @FXML private VBox resetConfirmPanel;

    @FXML
    public void initialize() {
        loadData();
        refreshUI();

        lblPlayerName.setOnMouseClicked(e -> startEditing());

        txtEditName.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)  confirmEdit();
            if (e.getCode() == KeyCode.ESCAPE) cancelEdit();
        });

        txtEditName.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) confirmEdit();
        });
    }

    private void startEditing() {
        txtEditName.setText(playerName);
        lblPlayerName.setVisible(false);
        lblClickHint.setVisible(false);
        txtEditName.setVisible(true);
        txtEditName.requestFocus();
        txtEditName.selectAll();
    }

    private void confirmEdit() {
        if (!txtEditName.isVisible()) return;
        String newName = txtEditName.getText().trim();
        if (!newName.isEmpty()) {
            playerName = newName;
            saveData();
        }
        txtEditName.setVisible(false);
        lblPlayerName.setVisible(true);
        lblClickHint.setVisible(true);
        refreshUI();
    }

    private void cancelEdit() {
        txtEditName.setVisible(false);
        lblPlayerName.setVisible(true);
        lblClickHint.setVisible(true);
    }

    @FXML
    private void showResetConfirm() {
        resetConfirmPanel.setVisible(true);
    }

    @FXML
    private void confirmReset() {
        wins = 0;
        losses = 0;
        saveData();
        refreshUI();
        resetConfirmPanel.setVisible(false);
    }

    @FXML
    private void cancelReset() {
        resetConfirmPanel.setVisible(false);
    }

    public static void loadData() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return;
        try (InputStream in = new FileInputStream(file)) {
            Properties props = new Properties();
            props.load(in);
            playerName = props.getProperty("name", "Player1");
            wins       = Integer.parseInt(props.getProperty("wins",   "0"));
            losses     = Integer.parseInt(props.getProperty("losses", "0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveData() {
        try (OutputStream out = new FileOutputStream(SAVE_FILE)) {
            Properties props = new Properties();
            props.setProperty("name",   playerName);
            props.setProperty("wins",   String.valueOf(wins));
            props.setProperty("losses", String.valueOf(losses));
            props.store(out, "Player Profile");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshUI() {
        lblPlayerName.setText(playerName);
        lblWins.setText("WINS: " + wins);
        lblLosses.setText("LOSSES: " + losses);
        int total = wins + losses;
        double winRate = total == 0 ? 0.0 : (wins * 100.0) / total;
        lblWinRate.setText(String.format("WIN RATE: %.1f%%", winRate));
    }

    @FXML
    private void backToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}