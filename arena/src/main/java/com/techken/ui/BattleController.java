package com.techken.ui;

import com.techken.MainApp;
import com.techken.ai.AILogic;
import com.techken.model.BaseCharacter;
import com.techken.skills.BaseSkill;
import com.techken.utils.CombatMath;
import com.techken.utils.SaveManager;
import javafx.event.ActionEvent;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class BattleController {
    // BATTLE.FXML UI stuff elements thingy
    @FXML private AnchorPane battleArenaRoot;
    @FXML private Label turnLabel;
    @FXML private Label playerNameLabel;
    @FXML private ProgressBar playerHealthBar;
    @FXML private Label playerHpLabel;
    @FXML private Label cpuNameLabel;
    @FXML private ProgressBar cpuHealthBar;
    @FXML private Label cpuHpLabel;
    @FXML private ImageView playerSpriteView;
    @FXML private ImageView cpuSpriteView;
    @FXML private Label battleLogLabel;
    @FXML private VBox dialogueBox;
    @FXML private GridPane buttonGrid;
    @FXML private Button skill1Btn;
    @FXML private Button skill2Btn;
    @FXML private Button skill3Btn;
    @FXML private Button quitBtn;
    @FXML private VBox gameOverPanel;
    @FXML private Label winnerLabel;
    @FXML private Label finalHpLabel;
    @FXML private Button backToMenuBtn;
    // THIS IS NEW CODE
    @FXML private javafx.scene.image.ImageView playerSprite;
    @FXML private javafx.scene.image.ImageView cpuSprite;

    // - Game State Stuff -
    private BaseCharacter playerCharacter;
    private BaseCharacter cpuCharacter;
    private final AILogic aiLogic = new AILogic();
    private final CombatMath combatMath = new CombatMath();
    private int turnCounter = 1;

    // Switching to enums cause the code is too long if lahi lahi functions
    private enum State { INTRO, PLAYER_TURN, CPU_TURN, NEXT_TURN, GAME_OVER }
    private State currentState = State.INTRO;

    @FXML
    public void initialize() {
        Random rand = new Random();
        int stageNumber = rand.nextInt(3) + 1;
        battleArenaRoot.getStyleClass().add("arena-" + stageNumber);
        System.out.println("Loaded Random Stage: ARENA " + stageNumber);
    }

    /**
     * To pass the characters na gi pili from CharacterSelectController (receiver ni)
     */
    public void initMatch(BaseCharacter playerCharacter, BaseCharacter cpuCharacter) {
        this.playerCharacter = playerCharacter;
        this.cpuCharacter = cpuCharacter;

        //Display character names to be used by label stuff
        playerNameLabel.setText(playerCharacter.getName());
        cpuNameLabel.setText(cpuCharacter.getName());

        // ---> NEW CODE HERE
        setSprite(playerSprite, playerCharacter.getName(), "idle");
        setSprite(cpuSprite, cpuCharacter.getName(), "idle");

        // addan lang nato ug null check in case we add someone with only 1 skill for test
        BaseSkill[] skills = playerCharacter.getSkills();
        if (skills[0] != null) {
            skill1Btn.setText(skills[0].getSkillName());
        }
        if (skills[1] != null) {
            skill2Btn.setText(skills[1].getSkillName());
        }
        if(skills[2] != null) {
            skill3Btn.setText(skills[2].getSkillName());
        }

        refreshHealthBars(); // ill add this later.
        setSkillButtonsDisabled(true); //for disabling turn based

        playerCharacter.setPose(BaseCharacter.Pose.STANCE);
        cpuCharacter.setPose(BaseCharacter.Pose.STANCE);
        loadCharacterSprite(playerCharacter, playerSpriteView);
        loadCharacterSprite(cpuCharacter, cpuSpriteView);

        MainApp.playMusic("OngoingBattleAudio.mp3");
        currentState = State.INTRO;
        battleLogLabel.setText(playerCharacter.getName() + " VS " + cpuCharacter.getName() + "!");
    }

    // onDialogueClickStart is our gamehandler, might have to rename this tho
    @FXML
    public void onDialogueClickStart() {
        switch(currentState) {
            case INTRO:
            case NEXT_TURN:
                startNextTurn();
                break;
            case CPU_TURN:
                executeCpuTurn();
                break;
            default:
                break; // do nothing if mag wait for PLAYER_TURN or GAME_OVER
        }
    }

    // adds to turn counter sa taas between health bar, also starting dialogue, allows buttons to work.
    private void startNextTurn() {
        if (currentState == State.NEXT_TURN) turnCounter++;
        turnLabel.setText("TURN " + turnCounter);
        battleLogLabel.setText("--- Turn " + " ---\nWhat will " + playerCharacter.getName() + " do?");
        setSkillButtonsDisabled(false);
        currentState = State.PLAYER_TURN;
    }


    // -----------------------------------
    //      SKILL BUTTON STUFF
    // -----------------------------------
    @FXML
    public void onSkill1() {
        executePlayerSkill(0);
    }

    @FXML
    public void onSkill2() {
        executePlayerSkill(1);
    }

    @FXML
    public void onSkill3() {
        executePlayerSkill(2);
    }

    @FXML
    public void onQuit() {
        navigateToMainMenu();
    }

    // -----------------------------------
    //     TURN BASED COMBAT HERE
    // -----------------------------------

    private void executePlayerSkill(int skillIndex) {
        if (currentState != State.PLAYER_TURN || playerCharacter == null || cpuCharacter == null) {
            return;
        }
        BaseSkill chosenSkill = playerCharacter.getSkills()[skillIndex];
        if (chosenSkill == null)
            return;

        setSkillButtonsDisabled(true);
        executeAction(playerCharacter, cpuCharacter, chosenSkill, "(Player)", State.CPU_TURN);
    }

    private void executeCpuTurn() {
        BaseSkill chosenSkill = aiLogic.selectSkill(cpuCharacter);
        executeAction(cpuCharacter, playerCharacter, chosenSkill, "(CPU)", State.NEXT_TURN);
    }

    private void executeAction(BaseCharacter attacker, BaseCharacter defender, BaseSkill skill, String tag, State nextState) {
        attacker.resetDefense();
        ImageView attackerView = attacker == playerCharacter ? playerSpriteView : cpuSpriteView;
        setCharacterPoseForSkill(attacker, attackerView, skill);

        // ---> NEW: Trigger the attack animation
        if (attacker == playerCharacter) {
            playSkillAnimation(playerSprite, attacker.getName(), skill.getSkillName());
        } else {
            playSkillAnimation(cpuSprite, attacker.getName(), skill.getSkillName());
        }

        playSoundEffect(skill.getSkillName());

        battleLogLabel.setText(applySkill(skill, attacker, defender, tag));
        refreshHealthBars();

        // if health of either not 0 then continue
        if(playerCharacter.getHealth() <= 0 || cpuCharacter.getHealth() <= 0) {
            currentState = State.GAME_OVER;
            handleGameOver();
        } else {
            currentState = nextState;
            restoreCharacterPoseAfterDelay();
        }
    }


    /**
     * Applies skill effects if value is > 0, and prints dialogue
     *    Not gonna use stringbuilder kay hugaw ang append
     */
    private String applySkill(BaseSkill skill, BaseCharacter attacker, BaseCharacter defender, String tag) {
        List<String> logLines = new ArrayList<>();

        // Line 1 : Announces the moves
        logLines.add(String.format("%s %s uses %s!", attacker.getName(), tag, skill.getSkillName()));

        // Apply damage and announce text
        int damage = combatMath.calculateDamage(skill, defender);
        if (damage > 0) { // add ta ug if because 'GuardAction' skills deal nothin
            defender.takeDamage(damage);
            logLines.add(String.format("Deals %d damage to %s", damage, defender.getName()));

            // Apply heal or lifesteal and announce text
            int healAmount = skill.getHealAmount(damage);
            if (healAmount > 0) { // we check 0 only if naay heal.
                int newHealth = Math.min(attacker.getHealth() + healAmount, attacker.getMaxHealth());
                attacker.setHealth(newHealth);
                logLines.add(String.format("%s recovers %d HP!", attacker.getName(), healAmount));
            }
        }

        // Apply defense boost (might need to change this, idk how it works,
        // ah nvm gets ko gamay it adds on to defense basically or minuses the final damage in CombatMath)
        int defBoost = skill.getDefenseBoost();
        if (defBoost > 0) { // we check 0 only if naay defense
            attacker.setDefense(attacker.getDefense() + defBoost);
            logLines.add(String.format("%s boosts defense by +%d", attacker.getName(), defBoost));
        }

        // Join all lines with line break
        return String.join("\n", logLines);
    }


    private void handleGameOver() {
        setSkillButtonsDisabled(true);
        MainApp.playMusic("BattleGameoverAudio.mp3");
        // ternary operator remember ? 'true' : 'false' , so meaning if playerhealth <=0 if true print cpu wins else
        // prints player wins.
        boolean playerWon = playerCharacter.getHealth() > 0;
        String winner = playerWon ? playerCharacter.getName() : cpuCharacter.getName();
        String winnerDisplay = playerWon ? "PLAYER WINS!" : "CPU WINS!";

        // Record win or loss to profile
        ProfileController.loadData();
        if (playerWon) {
            ProfileController.wins++;
            MainApp.playMusic("PlayerVictoryAudio.mp3");
        } else {
            ProfileController.losses++;
            MainApp.playMusic("PlayerDefeatAudio.mp3");
        }
        ProfileController.saveData();
        
        // Save match result to match history
        SaveManager.saveMatchResult(playerCharacter.getName(), cpuCharacter.getName(), winner);

        if (playerWon) {
            playerCharacter.setPose(BaseCharacter.Pose.WIN);
            cpuCharacter.setPose(BaseCharacter.Pose.LOSE);
        } else {
            playerCharacter.setPose(BaseCharacter.Pose.LOSE);
            cpuCharacter.setPose(BaseCharacter.Pose.WIN);
        }
        refreshCharacterSprite(playerCharacter, playerSpriteView);
        refreshCharacterSprite(cpuCharacter, cpuSpriteView);

        battleLogLabel.setText("=== FIGHT OVER === " + winnerDisplay);
        winnerLabel.setText(winnerDisplay);

        // mao ni mo print sa summary hp when match concludes
        finalHpLabel.setText(
                playerCharacter.getName() + " HP: " + Math.max(0, playerCharacter.getHealth())
                        + "/" + playerCharacter.getMaxHealth()
                        + "\n"
                        + cpuCharacter.getName() + " HP: " + Math.max(0, cpuCharacter.getHealth())
                        + "/" + cpuCharacter.getMaxHealth());
        gameOverPanel.toFront();
        gameOverPanel.setVisible(true);
    }


    // -----------------------------------
    //     UI HELPER STUFF
    // -----------------------------------

    private void refreshHealthBars() {
        int playerHP = Math.max(0, playerCharacter.getHealth());
        int playerMaxHP = playerCharacter.getMaxHealth();
        playerHealthBar.setProgress((double) playerHP / playerMaxHP);
        playerHpLabel.setText("HP: " + playerHP + "/" + playerMaxHP);

        int cpuHP = Math.max(0, cpuCharacter.getHealth());
        int cpuMaxHP = cpuCharacter.getMaxHealth();
        cpuHealthBar.setProgress((double) cpuHP / cpuMaxHP);
        cpuHpLabel.setText("HP: " + cpuHP + "/" + cpuMaxHP);
    }

    private void setSkillButtonsDisabled(boolean disabled) {
        skill1Btn.setDisable(disabled);
        skill2Btn.setDisable(disabled);
        skill3Btn.setDisable(disabled);
    }

    private void loadCharacterSprite(BaseCharacter character, ImageView view) {
        if (character == null || view == null) {
            return;
        }
        applySpriteOrientation(view);
        try {
            Image sprite = new Image(getClass().getResourceAsStream(character.getSpritePath(character.getCurrentPose())));
            view.setImage(sprite);
        } catch (Exception e) {
            view.setImage(null);
        }
    }

    private void refreshCharacterSprite(BaseCharacter character, ImageView view) {
        if (character == null || view == null) {
            return;
        }
        applySpriteOrientation(view);
        try {
            Image sprite = new Image(getClass().getResourceAsStream(character.getSpritePath(character.getCurrentPose())));
            view.setImage(sprite);
        } catch (Exception e) {
            // keep existing image if pose asset is missing
        }
    }

    private void applySpriteOrientation(ImageView view) {
        if (view == cpuSpriteView) {
            view.setScaleX(-1);
        } else {
            view.setScaleX(1);
        }
    }

    private void setCharacterPoseForSkill(BaseCharacter attacker, ImageView attackerView, BaseSkill skill) {
        if (attacker == null || attackerView == null) {
            return;
        }
        BaseCharacter.Pose pose = attacker.getPoseForSkill(skill);
        attacker.setPose(pose);
        refreshCharacterSprite(attacker, attackerView);
    }

    private void restoreCharacterPoseAfterDelay() {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.75));
        pause.setOnFinished(event -> {
            if (currentState != State.GAME_OVER) {
                playerCharacter.setPose(BaseCharacter.Pose.STANCE);
                cpuCharacter.setPose(BaseCharacter.Pose.STANCE);
                refreshCharacterSprite(playerCharacter, playerSpriteView);
                refreshCharacterSprite(cpuCharacter, cpuSpriteView);
            }
        });
        pause.play();
    }

    /**
     * Menu Navigation (Back To Menu Button)
     *
     * need to wrapper function kay fxml can't do fxml statement stuff
     */
    @FXML
    public void backToMenu() {
        navigateToMainMenu();
    }

    private void navigateToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 720);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage) battleArenaRoot.getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- NEW CODE HERE ---

    // Automatically finds the image based on Name + State (e.g., "HEIHACHI MISHIMA" + "idle" -> "heihachi mishima_idle.png")
    private void setSprite(javafx.scene.image.ImageView targetView, String characterName, String state) {
        // Format the name to match your files (lowercase)
        String fileName = characterName.toLowerCase() + "_" + state + ".png";
        try {
            System.out.println("ATTEMPTING TO LOAD: /Images/" + fileName); // Let's see what it's looking for
            javafx.scene.image.Image img = new javafx.scene.image.Image(getClass().getResourceAsStream("/Images/" + fileName));
            targetView.setImage(img);
        } catch (Exception e) {
            System.out.println("FAILED TO LOAD: " + fileName);
            e.printStackTrace();
        }
    }

    // Swaps to attack image, waits 0.6 seconds, then snaps back to idle
    private void playAttackAnimation(javafx.scene.image.ImageView attackerSprite, String characterName) {
        setSprite(attackerSprite, characterName, "attack");

        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(0.6));
        pause.setOnFinished(event -> setSprite(attackerSprite, characterName, "idle"));
        pause.play();
    }

    private void playSkillAnimation(javafx.scene.image.ImageView actorSprite, String characterName, String skillName) {
        // This takes "Vajra Block" and turns it into "vajra block"
        String stateName = skillName.toLowerCase();

        setSprite(actorSprite, characterName, stateName);

        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(0.6));
        pause.setOnFinished(event -> setSprite(actorSprite, characterName, "idle"));
        pause.play();
    }

    // --- SOUND EFFECT HELPER ---
    private void playSoundEffect(String skillName) {
        String fileName = skillName.toLowerCase() + ".mp3";

        try {

            java.net.URL audioUrl = getClass().getResource("/Sounds/" + fileName);
            if (audioUrl != null) {
                javafx.scene.media.AudioClip clip = new javafx.scene.media.AudioClip(audioUrl.toExternalForm());
                clip.play();
            } else {
                System.out.println("Could not find sound: " + fileName);
            }
        } catch (Exception e) {
            System.out.println("Error playing sound: " + fileName);
        }
    }
}