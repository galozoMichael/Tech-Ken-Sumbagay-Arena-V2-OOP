package com.techken.ui;

import com.techken.ai.AILogic;
import com.techken.model.BaseCharacter;
import com.techken.skills.BaseSkill;
import com.techken.utils.CombatMath;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.PauseTransition;
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
    @FXML private ImageView playerImageView;
    @FXML private ImageView cpuImageView;

    private Image playerIdle, playerHurt;
    private Image cpuIdle, cpuHurt;

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

        // 1. Existing Label logic
        playerNameLabel.setText(playerCharacter.getName());
        cpuNameLabel.setText(cpuCharacter.getName());

        // 2. NEW: Character Image Loading Logic
        try {
            // Clean names to match file naming (e.g., "DevilJin" -> "deviljin")
            String pName = playerCharacter.getName().toLowerCase();
            String cName = cpuCharacter.getName().toLowerCase();

            playerIdle = new Image(getClass().getResourceAsStream("/Images/" + pName + "-Idle.png"));
            playerHurt = new Image(getClass().getResourceAsStream("/Images/" + pName + "-Hurt.png"));

            cpuIdle = new Image(getClass().getResourceAsStream("/Images/" + cName + "-Idle.png"));
            cpuHurt = new Image(getClass().getResourceAsStream("/Images/" + cName + "-Hurt.png"));

            // Set initial idle images
            playerImageView.setImage(playerIdle);
            cpuImageView.setImage(cpuIdle);

            // Mirror the CPU so they face the player
            cpuImageView.setScaleX(-1);

        } catch (Exception e) {
            System.out.println("Image loading failed: " + e.getMessage());
        }

        // 3. Existing Skill Button logic
        BaseSkill[] skills = playerCharacter.getSkills();
        if (skills[0] != null) skill1Btn.setText(skills[0].getSkillName());
        if (skills[1] != null) skill2Btn.setText(skills[1].getSkillName());
        if (skills[2] != null) skill3Btn.setText(skills[2].getSkillName());

        refreshHealthBars();
        setSkillButtonsDisabled(true);

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

    private void executeAction(BaseCharacter attacker, BaseCharacter defender, BaseSkill skill, String tag,  State nextState) {
        attacker.resetDefense();

        battleLogLabel.setText(applySkill(skill, attacker, defender, tag));
        refreshHealthBars();

        // if health of either not 0 then continue
        if(playerCharacter.getHealth() <= 0 || cpuCharacter.getHealth() <= 0) {
            currentState = State.GAME_OVER;
            handleGameOver();
        } else {
            currentState = nextState;
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
        // ternary operator remember ? 'true' : 'false' , so meaning if playerhealth <=0 if true print cpu wins else
        // prints player wins.
        String winner = playerCharacter.getHealth() <= 0 ? "CPU WINS!" : "PLAYER WINS!";
        battleLogLabel.setText("=== FIGHT OVER === " + winner);
        winnerLabel.setText(winner);

        // mao ni mo print sa summary hp when match concludes
        finalHpLabel.setText(
                playerCharacter.getName() + "HP: " + Math.max(0, playerCharacter.getHealth())
                + "/" + playerCharacter.getMaxHealth()
                + "\n"
                + cpuCharacter.getName() + " HP: " + Math.max(0, cpuCharacter.getHealth())
                + "/" + cpuCharacter.getMaxHealth());

        gameOverPanel.setVisible(true);
    }



    private void triggerHurtAnimation(ImageView view, Image hurtImg, Image idleImg) {
        view.setImage(hurtImg);

        // Wait for 0.3 seconds then switch back to idle
        PauseTransition pause = new PauseTransition(Duration.millis(300));
        pause.setOnFinished(event -> view.setImage(idleImg));
        pause.play();
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
}