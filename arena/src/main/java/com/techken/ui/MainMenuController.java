package com.techken.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class MainMenuController {

    @FXML
    private void onPlay() {
        // TODO: switch to character select screen
        System.out.println("Play clicked");
    }

    @FXML
    private void onExit() {
        Platform.exit();
    }
}