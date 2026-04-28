package com.techken.ui;

import com.techken.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class CharacterSelectController {

    @FXML private ImageView characterPreview; // The big image on the left

    // 1. THE VARIABLES AT THE TOP
    @FXML private javafx.scene.layout.VBox infoPanel;
    @FXML private javafx.scene.control.Label charNameLabel;
    @FXML private javafx.scene.control.Label charBioLabel;
    @FXML private javafx.scene.control.Label charStatsLabel;

    @FXML
    public void initialize() {
        MainApp.playMusic("menutestaudio.mp3");
    }

    @FXML
    public void selectFighter(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String id = clickedButton.getId();

        // Print to the console so you can track clicks
        System.out.println("Selected Fighter: " + id);

        // Turn the panel on!
        infoPanel.setVisible(true);

        // Inject the lore, archetypes, and stats based on the design doc!
        if ("btnHeihachi".equals(id)) {
            charNameLabel.setText("HEIHACHI MISHIMA");
            charBioLabel.setText("ARCHETYPE: TANK\nHigh Health & Defense, Heavy Attacks, low speed.");
            charStatsLabel.setText("POWER: S | SPEED: C | TECH: B");
        }
        else if ("btnDevilJin".equals(id)) {
            charNameLabel.setText("DEVIL JIN");
            charBioLabel.setText("ARCHETYPE: BERSERKER\nPolymorphism: Attack damage increases significantly when HP drops below 30%.");
            charStatsLabel.setText("POWER: A | SPEED: S | TECH: B");
        }
        else if ("btnJohnnyCage".equals(id)) {
            charNameLabel.setText("JOHNNY CAGE");
            charBioLabel.setText("ARCHETYPE: SPEEDSTER\nHigh Evasion & Turn Priority. Strikes first.");
            charStatsLabel.setText("POWER: C | SPEED: S | TECH: A");
        }
        else if ("btnReptile".equals(id)) {
            charNameLabel.setText("REPTILE");
            charBioLabel.setText("ARCHETYPE: DEBUFFER\nUses HealthSteal to simulate poison/acid, draining enemy life to heal himself.");
            charStatsLabel.setText("POWER: B | SPEED: A | TECH: S");
        }
        else if ("btnScorpion".equals(id)) {
            charNameLabel.setText("SCORPION");
            charBioLabel.setText("ARCHETYPE: GLASS CANNON\nHigh Base Attack power utilizing strong offensive moves.");
            charStatsLabel.setText("POWER: S | SPEED: A | TECH: C");
        }
        else if ("btnLiuKang".equals(id)) {
            charNameLabel.setText("LIU KANG");
            charBioLabel.setText("ARCHETYPE: BALANCED\nPerfectly balanced stats with moderate damage output.");
            charStatsLabel.setText("POWER: B | SPEED: B | TECH: B");
        }
        else {
            // For btnChar7 and btnChar8 (Empty slots)
            charNameLabel.setText("LOCKED SLOT");
            charBioLabel.setText("DLC Character incoming...\nStay tuned for the next challenger!");
            charStatsLabel.setText("POWER: ? | SPEED: ? | TECH: ?");
        }
    }

    // Handles returning to the main menu
    @FXML
    public void BackToMainMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 720);

            // Re-apply the stylesheet
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}