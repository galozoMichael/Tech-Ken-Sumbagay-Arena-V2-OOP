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

    // - Game State Stuff -
    private BaseCharacter playerCharacter;
    private BaseCharacter cpuCharacter;
    private final AILogic aiLogic = new AILogic();
    private final CombatMath combatMath = new CombatMath();
    private int turnCounter = 1;

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

        playerNameLabel.setText(playerCharacter.getName());
        cpuNameLabel.setText(cpuCharacter.getName());

        BaseSkill[] skills = playerCharacter.getSkills();
        if (skills[0] != null) skill1Btn.setText(skills[0].getSkillName());
        if (skills[1] != null) skill2Btn.setText(skills[1].getSkillName());
        if (skills[2] != null) skill3Btn.setText(skills[2].getSkillName());

        refreshHealthBars();
        setSkillButtonsDisabled(true);

        currentState = State.INTRO;
        battleLogLabel.setText(playerCharacter.getName() + " VS " + cpuCharacter.getName() + "!");
    }

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
                break;
        }
    }

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
    @FXML public void onSkill1() { executePlayerSkill(0); }
    @FXML public void onSkill2() { executePlayerSkill(1); }
    @FXML public void onSkill3() { executePlayerSkill(2); }
    @FXML public void onQuit()   { navigateToMainMenu(); }

    // -----------------------------------
    //     TURN BASED COMBAT HERE
    // -----------------------------------
    private void executePlayerSkill(int skillIndex) {
        if (currentState != State.PLAYER_TURN || playerCharacter == null || cpuCharacter == null) return;
        BaseSkill chosenSkill = playerCharacter.getSkills()[skillIndex];
        if (chosenSkill == null) return;

        setSkillButtonsDisabled(true);
        executeAction(playerCharacter, cpuCharacter, chosenSkill, "(Player)", State.CPU_TURN);
    }

    private void executeCpuTurn() {
        BaseSkill chosenSkill = aiLogic.selectSkill(cpuCharacter);
        executeAction(cpuCharacter, playerCharacter, chosenSkill, "(CPU)", State.NEXT_TURN);
    }

    private void executeAction(BaseCharacter attacker, BaseCharacter defender, BaseSkill skill, String tag, State nextState) {
        attacker.resetDefense();
        battleLogLabel.setText(applySkill(skill, attacker, defender, tag));
        refreshHealthBars();

        if (playerCharacter.getHealth() <= 0 || cpuCharacter.getHealth() <= 0) {
            currentState = State.GAME_OVER;
            handleGameOver();
        } else {
            currentState = nextState;
        }
    }

    /**
     * Applies skill effects if value is > 0, and prints dialogue
     */
    private String applySkill(BaseSkill skill, BaseCharacter attacker, BaseCharacter defender, String tag) {
        List<String> logLines = new ArrayList<>();

        logLines.add(String.format("%s %s uses %s!", attacker.getName(), tag, skill.getSkillName()));

        int damage = combatMath.calculateDamage(skill, defender);
        if (damage > 0) {
            defender.takeDamage(damage);
            logLines.add(String.format("Deals %d damage to %s", damage, defender.getName()));

            int healAmount = skill.getHealAmount(damage);
            if (healAmount > 0) {
                int newHealth = Math.min(attacker.getHealth() + healAmount, attacker.getMaxHealth());
                attacker.setHealth(newHealth);
                logLines.add(String.format("%s recovers %d HP!", attacker.getName(), healAmount));
            }
        }

        int defBoost = skill.getDefenseBoost();
        if (defBoost > 0) {
            attacker.setDefense(attacker.getDefense() + defBoost);
            logLines.add(String.format("%s boosts defense by +%d", attacker.getName(), defBoost));
        }

        return String.join("\n", logLines);
    }

    private void handleGameOver() {
        setSkillButtonsDisabled(true);

        boolean playerWon = playerCharacter.getHealth() > 0;
        String winner = playerWon ? "PLAYER WINS!" : "CPU WINS!";

        // Record win or loss to profile
        ProfileController.loadData();
        if (playerWon) {
            ProfileController.wins++;
        } else {
            ProfileController.losses++;
        }
        ProfileController.saveData();

        battleLogLabel.setText("=== FIGHT OVER === " + winner);
        winnerLabel.setText(winner);
        finalHpLabel.setText(
                playerCharacter.getName() + " HP: " + Math.max(0, playerCharacter.getHealth())
                        + "/" + playerCharacter.getMaxHealth()
                        + "\n"
                        + cpuCharacter.getName() + " HP: " + Math.max(0, cpuCharacter.getHealth())
                        + "/" + cpuCharacter.getMaxHealth());

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

    /**
     * Menu Navigation (Back To Menu Button)
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