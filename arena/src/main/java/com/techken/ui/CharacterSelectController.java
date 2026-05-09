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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Random;
import javafx.scene.media.AudioClip;

public class CharacterSelectController {

    @FXML private VBox infoPanel;
    @FXML private Label charNameLabel;
    @FXML private Label charBioLabel;
    @FXML private Label charStatsLabel;
    @FXML private Button backBtn;
    @FXML private Button lockInBtn;
    @FXML private ImageView characterPreview;


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
                    "Extremely durable with massive health and high defense.", "HP: 140 | DEF: 22 | MAX DMG: 28",
                    "/images/characters/heihachi.png",
                    "/audio/announcer_heihachi.mp3");
            case "btnDevilJin" -> updateUI("DEVIL JIN", "BRUISER",
                    "Combines solid stats with life-stealing attacks to outlast opponents.", "HP: 110 | DEF: 15 | MAX DMG: 25",
                    "/images/characters/heihachi.png", // /images/characters/deviljin.png * this is for voice test restore later
                    "/audio/announcer_deviljin.mp3");
            case "btnJohnnyCage" -> updateUI("JOHNNY CAGE", "BRAWLER",
                    "Aggressive fighter with high-damage combos. Frail defense.", "HP: 105 | DEF: 12 | MAX DMG: 28",
                    "/images/characters/heihachi.png", // /images/characters/johnnycage.png
                    "/audio/announcer_johnny.mp3");
            case "btnReptile" -> updateUI("REPTILE", "SUSTAIN",
                    "Drains enemy life to heal himself. Masters a battle of attrition.", "HP: 100 | DEF: 10 | MAX DMG: 18",
                    "/images/characters/heihachi.png", // /images/characters/reptile.png
                    "/audio/announcer_reptile.mp3");
            case "btnScorpion" -> updateUI("SCORPION", "AGGRESSOR",
                    "Relentless offense with devastating attacks, but vulnerable to counters.", "HP: 105 | DEF: 10 | MAX DMG: 30",
                    "/images/characters/heihachi.png", // /images/characters/scorpion.png
                    "/audio/announcer_scorpion.mp3");
            case "btnTiger" -> updateUI("TIGER", "BALANCED",
                    "Well-rounded fighter with solid health, defense, and consistent damage.", "HP: 120 | DEF: 15 | MAX DMG: 25",
                    "/images/characters/heihachi.png", // /images/characters/tiger.png
                    "/audio/announcer_tiger.mp3");
            case "btnManny" -> updateUI("MANNY PACQUAIO", "GLASS CANNON",
                    "Packs a devastating punch with extreme damage, but has the lowest health.",
                    "HP: 95 | DEF: 12 | MAX DMG: 35",
                    "/images/characters/heihachi.png", //images/characters/manny.png
                    "/audio/announcer_manny.mp3");
            case "btnRandomize" -> updateUI("???", "RANDOM",
                    "A random fighter will be selected.",
                    "HP: ??? | DEF: ??? | MAX DMG: ???",
                    "/images/characters/heihachi.png", "/audio/announcer_random.mp3");
            default -> {
                charNameLabel.setText("LOCKED");            // just leave this be
                charBioLabel.setText("Wa himuon pani");
                charStatsLabel.setText("HP: ? | DEF: ? | MAX DMG: ?");
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
            case "btnManny" -> new MannyPacquiao();
            case "btnRandomize" -> selectCPUCharacter();
            default -> null;
        };
    }

    /**
     * CPU Character random pick, basically mo random from 1-6 lol also for player if they pick random.
     */
    private BaseCharacter selectCPUCharacter() {
        int pick = random.nextInt(7) + 1;
        return switch (pick) {
            case 1 -> new HeihachiMisihima();
            case 2 -> new DevilJin();
            case 3 -> new JohnnyCage();
            case 4 -> new Reptile();
            case 5 -> new Scorpion();
            case 6 -> new Tiger();
            default -> new MannyPacquiao();
        };
    }


    @FXML
    public void lockIn() {
        if (selectedFighterId == null) {
            infoPanel.setVisible(true);
            updateUI("NO FIGHTER SELECTED", "", "PILI FIRST...", "POWER: ? | SPEED: ?", null, null);
            System.out.println("Player not pick wtf!!");
            return;
        }

        /*
        *  Check in case blank or not picked and pressed the lock in. para di mohimo og object
        * */
        BaseCharacter playerCharacter = createCharacterFromId(selectedFighterId);
        if (playerCharacter == null) {
            updateUI("ERROR", "", "Invalid fighter selection", "", null, null);
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
            battleController.initMatch(playerCharacter, cpuCharacter);
            Stage stage = (Stage) lockInBtn.getScene().getWindow();
            stage.setMaximized(false); // fix sa bug na mo minimize when switching from characterselect.fxml to battle.fxml
            stage.setScene(scene);
            stage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void updateUI(String name, String archetype, String mechanic, String stats, String imagePath, String soundPath) {
        charNameLabel.setText(name);
        charBioLabel.setText("ARCHETYPE: " + archetype + "\n" + mechanic);
        charStatsLabel.setText(stats);

        try {
            Image portrait = new Image(getClass().getResourceAsStream(imagePath));
            characterPreview.setImage(portrait);
            characterPreview.setVisible(true);
            // for announcer sound
            if (soundPath != null) {
                String audioUrl = getClass().getResource(soundPath).toExternalForm();
                AudioClip announcerVoice = new AudioClip(audioUrl);
                announcerVoice.play();
            }

        } catch (Exception e) {
            characterPreview.setVisible(false);
        }
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