package com.techken.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class MainMenuController {

    @FXML private void Play() { System.out.println("Start Game"); }

    @FXML private void CharacterSelect() { System.out.println("Character Select"); }

    @FXML private void HowToPlay() { System.out.println("How To Play"); }

    @FXML private void Credits() { System.out.println("Credits"); }

    @FXML private void Settings() { System.out.println("Settings"); }

    @FXML private void Exit() { Platform.exit(); }
}