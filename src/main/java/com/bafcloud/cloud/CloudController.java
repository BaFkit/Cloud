package com.bafcloud.cloud;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CloudController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}