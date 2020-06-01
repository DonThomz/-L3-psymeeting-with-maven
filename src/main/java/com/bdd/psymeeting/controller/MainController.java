package com.bdd.psymeeting.controller;

import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public VBox mainContent;

    public StackPane stackPane;
    public static VBox mainContentStatic;
    public static StackPane stackPaneStatic;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainContentStatic = mainContent;
        stackPaneStatic = stackPane;
    }
}
