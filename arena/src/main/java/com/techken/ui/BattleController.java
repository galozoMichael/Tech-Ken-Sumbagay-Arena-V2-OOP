package com.techken.ui;

import com.techken.model.BaseCharacter;
import com.techken.skills.BaseSkill;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Random;

public class BattleController {
    // BATTLE.FXML UI stuff elements thingy
    @FXML private AnchorPane battleArenaRoot;
    @FXML private Label turnLabel;
    @FXML private Label playerNameLabel;
    @FXML private ProgressBar playerHealthLabel;
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

    // Game state stuff
    private BaseCharacter playerCharacter;
    private BaseCharacter cpuCharacter;
    //private final AILogic aiLogic = new AILogic();
    //private final CombatMath combatMath = new CombatMath();
    private int turnCounter = 1;
    private boolean gameOver = false;


    // state flags paras turn stuff
    private boolean waitingForCpuTurn = false;
    private boolean waitingForNextTurn = false;
    private int introStep = 0; // for da intro message stuff tracker



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

        //Display character names to be used by label stuf
        playerNameLabel.setText(playerCharacter.getName());
        cpuNameLabel.setText(cpuCharacter.getName());

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

        //refreshHealthBars(); // ill add this later.
        //setSkillButtonsDisabled(true); //for disabling turn based

        introStep = 1;
        battleLogLabel.setText(playerCharacter.getName() + " VS " + cpuCharacter.getName() + "!");
    }

    // Have to add para di mo crash xd
    @FXML
    public void onDialogueClickStart() {
        System.out.print("lol");
    }
    @FXML
    public void onSkill1() {
        //executePlayerSkill(0);
        System.out.print("lol");
    }

    @FXML
    public void onSkill2() {
        //executePlayerSkill(1);
        System.out.print("lol");
    }

    @FXML
    public void onSkill3() {
        //executePlayerSkill(2);
        System.out.print("lol");
    }

    @FXML
    public void onQuit() {
        // navigateToMainMenu();
        System.out.print("lol");
    }

    @FXML
    public void backToMenu() {
        // navigateToMainMenu();
        System.out.print("lol");
    }


    /** TODO : onDialogueClickStart (game handler progression), IntroStart() - match start messages kato dialogue
     *  SkillButton handlers (onSkill1, onSkill2, onSkill3), enablePlayerTurn(), setSkillButtonDisabled() - for turn based combat logics.
     *  executePlayerSkill() - used in onSkill also disables button after turn, executeCpuTurn(), resolveSkill(), nextTurn(), handleGameOver()
     *  UI HELPERS: refreshHealthBars(), setSkillButtonDisabled(), checkGameOver(), showGameOverPanel(), backToMenu() FXML WRAPPER paras victory or lose dialogue.
     *  nagivateToMainMenu() - to go back to menu called by backtoMenu()
     *  Need to summarize or simplify functions further since too many
     */

    @FXML
    public void handleAttack(ActionEvent event) {
        System.out.println("Player chose ATTACK!");
    }

    @FXML
    public void handleDefend(ActionEvent event) {
        System.out.println("Player chose DEFEND!");
    }

    @FXML
    public void handleSpecial(ActionEvent event) {
        System.out.println("Player chose SPECIAL!");
    }

    @FXML
    public void handleRun(ActionEvent event) {
        battleLogLabel.setText("Got away safely!");
        System.out.println("Player chose RUN!");
    }
}