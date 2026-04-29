package com.techken.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import java.util.Random;

public class BattleController {


    @FXML private AnchorPane battleArenaRoot;

    private String playerFighterId;


    @FXML private Label playerNameLabel;
    @FXML private Label enemyNameLabel;
    @FXML private ProgressBar playerHealthBar;
    @FXML private ProgressBar enemyHealthBar;
    @FXML private Label battleLogLabel;


    @FXML
    public void initialize() {
        Random rand = new Random();
        int stageNumber = rand.nextInt(3) + 1;


        battleArenaRoot.getStyleClass().add("arena-" + stageNumber);

        System.out.println("Loaded Random Stage: ARENA " + stageNumber);
    }


    public void initData(String fighterId) {
        this.playerFighterId = fighterId;
        String cleanName = playerFighterId.replace("btn", "").toUpperCase();
        playerNameLabel.setText(cleanName);

        // NEW: Set the dialogue box text!
        battleLogLabel.setText("What will\n" + cleanName + " do?");
    }


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