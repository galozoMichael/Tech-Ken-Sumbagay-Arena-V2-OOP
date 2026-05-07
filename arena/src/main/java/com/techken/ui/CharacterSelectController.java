package com.techken.ui;

import com.techken.MainApp;
import com.techken.model.BaseCharacter;
import com.techken.model.fighters.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Random;

public class CharacterSelectController {

    @FXML private VBox infoPanel;
    @FXML private Label charNameLabel;
    @FXML private Label charBioLabel;
    @FXML private Label charStatsLabel;
    @FXML private Button backBtn;
    @FXML private Button lockInBtn;

    private String selectedFighterId = null; /* kinsay gi pili lol */
    private final Random random = new Random(); /* this for cpu random pick */

    @FXML
    public void selectFighter(ActionEvent event) {
        String id = ((Button) event.getSource()).getId();
        selectedFighterId = id; /* mao ni ang id sa gi pili btw. */
        infoPanel.setVisible(true);
        // TODO: need to fix this, I removed speed feature so kaylangan sa need e revise.
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
            case "btnTiger" -> updateUI("TIGER", "BALANCED",
                    "Perfectly balanced stats with moderate damage output.", "POWER: B | SPEED: B");
            default -> {
                charNameLabel.setText("LOCKED");
                charBioLabel.setText("Wa himuon pani");
                charStatsLabel.setText("POWER: ? | SPEED: ?");
            }
        }
    }

    /**
     * Mo make og object from BaseCharacter after lock in button confirmed
     */

    private BaseCharacter createCharacterFromId(String id) {
        return switch (id) {
            case "btnHeihachi" -> new HeihachiMisihima();
            case "btnDevilJin" -> new DevilJin();
            case "btnJohnnyCage" -> new JohnnyCage();
            case "btnReptile" -> new Reptile();
            case "btnScorpion" -> new Scorpion();
            case "btnTiger" -> new Tiger();
            default -> null;
        };
    }

    /**
     * CPU Character random pick, basically mo random from 1-6 lol
     */
    private BaseCharacter selectCPUCharacter() {
        int pick = random.nextInt(6) + 1;
        return switch (pick) {
            case 1 -> new HeihachiMisihima();
            case 2 -> new DevilJin();
            case 3 -> new JohnnyCage();
            case 4 -> new Reptile();
            case 5 -> new Scorpion();
            default -> new Tiger();
        };
    }


    @FXML
    public void lockIn() {
        if (selectedFighterId == null) {
            infoPanel.setVisible(true);
            updateUI("NO FIGHTER SELECTED", "", "PILI FIRST...", "POWER: ? | SPEED: ?");
            System.out.println("Player not pick wtf!!");
            return;
        }

        /*
        *  Check in case blank or not picked and pressed the lock in. para di mohimo og object
        * */
        BaseCharacter playerCharacter = createCharacterFromId(selectedFighterId);
        if (playerCharacter == null) {
            updateUI("ERROR", "", "Invalid fighter selection", "");
            return;
        }

        // same ranis taas needed para e pass natos battlecontroller.
        BaseCharacter cpuCharacter = selectCPUCharacter();

        // for logging purposes maybe remove this latur
        System.out.println("Player selected: " + playerCharacter.getName());
        System.out.println("CPU Selected: " + cpuCharacter.getName());


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Battle.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            // this to pass the characters to BattleController.java
            BattleController battleController = loader.getController();
            //battleController.initMatch(playerCharacter, cpuCharacter)
            Stage stage = (Stage) lockInBtn.getScene().getWindow();
            stage.setMaximized(false); // fix sa bug na mo minimize when switching from characterselect.fxml to battle.fxml
            stage.setScene(scene);
            stage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void updateUI(String name, String archetype, String mechanic, String stats) {
        charNameLabel.setText(name);
        charBioLabel.setText("ARCHETYPE: " + archetype + "\n" + mechanic);
        charStatsLabel.setText(stats);
    }

    @FXML
    public void initialize() {
        MainApp.playMusic("SelectYourFighter.mp3");
    }

    @FXML
    public void backToMenu() {
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
}