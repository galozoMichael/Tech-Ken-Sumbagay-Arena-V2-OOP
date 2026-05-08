package com.techken.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.io.*;
import java.util.Properties;

public class ProfileController {

    // Save file location
    private static final String SAVE_FILE = "profile.properties";

    // Data
    private String playerName = "Player1";
    private int wins = 0;
    private int losses = 0;

    @FXML private Button backBtn;
    @FXML private Label lblPlayerName;
    @FXML private TextField txtEditName;
    @FXML private Label lblWins;
    @FXML private Label lblLosses;
    @FXML private Label lblWinRate;
    @FXML private Label lblClickHint;

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

    private void loadData() {
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

    private void saveData() {
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

    // function paras battlecontroller in order to save wins and loss since private tanan functions dri
    public static void updateMatchResult(boolean playerWon) {
        File file = new File(SAVE_FILE);
        Properties props = new Properties();

        // Loads first if file exist
        if (file.exists()) {
            try (InputStream in = new FileInputStream(file)) {
                props.load(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // if not exist then default vals:
        String name = props.getProperty("name", "Player1");
        int wins = Integer.parseInt(props.getProperty("wins", "0"));
        int losses = Integer.parseInt(props.getProperty("losses", "0"));

        if (playerWon) {
            wins++;
        } else {
            losses++;
        }

        try (OutputStream out = new FileOutputStream(file)) {
            props.setProperty("name",   name);
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