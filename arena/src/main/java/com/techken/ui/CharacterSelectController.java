package com.techken.ui;

import com.techken.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CharacterSelectController {

    @FXML private VBox infoPanel;
    @FXML private Label charNameLabel;
    @FXML private Label charBioLabel;
    @FXML private Label charStatsLabel;
    @FXML private Button backBtn;

    @FXML
    public void selectFighter(ActionEvent event) {
        String id = ((Button) event.getSource()).getId();
        infoPanel.setVisible(true);

        switch (id) {
            case "btnHeihachi" -> updateUI("HEIHACHI MISHIMA", "TANK",
                    "High Health & Defense, Heavy Attacks, low speed.", "POWER: S | SPEED: C");
            case "btnDevilJin" -> updateUI("DEVIL JIN", "BERSERKER",
                    "Attack damage increases significantly when HP drops below 30%.", "POWER: A | SPEED: S");
            case "btnJohnnyCage" -> updateUI("JOHNNY CAGE", "SPEEDSTER",
                    "High Evasion & Turn Priority. Strikes first.", "POWER: C | SPEED: S");
            case "btnReptile" -> updateUI("REPTILE", "DEBUFFER",
                    "Uses HealthSteal to drain enemy life to heal himself.", "POWER: B | SPEED: A");
            case "btnScorpion" -> updateUI("SCORPION", "GLASS CANNON",
                    "High Base Attack power utilizing strong offensive moves.", "POWER: S | SPEED: A");
            case "btnLiuKang" -> updateUI("LIU KANG", "BALANCED",
                    "Perfectly balanced stats with moderate damage output.", "POWER: B | SPEED: B");
            default -> {
                charNameLabel.setText("LOCKED");
                charBioLabel.setText("Wa himuon pani");
                charStatsLabel.setText("POWER: ? | SPEED: ?");
            }
        }
    }

    private void updateUI(String name, String archetype, String mechanic, String stats) {
        charNameLabel.setText(name);
        charBioLabel.setText("ARCHETYPE: " + archetype + "\n" + mechanic);
        charStatsLabel.setText(stats);
    }

    @FXML
    public void initialize() {
        MainApp.playMusic("SelectYourCharacter.mp3");
    }

    @FXML
    public void backToMenu() {
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