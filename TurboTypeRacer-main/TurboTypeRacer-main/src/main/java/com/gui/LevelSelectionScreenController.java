package com.gui;

import com.game.Level;
import com.game.LevelManager;
import com.game.LoginManager;
import com.game.Music;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LevelSelectionScreenController {
    @FXML
    private ImageView goBackButton;
    @FXML
    private TilePane tilePane;
    private Stage stage;
    private Parent root;

    @FXML
    void animationPop(MouseEvent event) {
        ((ImageView)event.getSource()).setStyle("-fx-effect: dropShadow(gaussian, " + "#E34255" + ", 28, 0.7, 0, 0)");
    }
    @FXML
    void animationPopUp(MouseEvent event) {
        ((ImageView)event.getSource()).setStyle(null);
    }

    @FXML
    void returnToMain(MouseEvent event) throws IOException {
        Music.playButtonSound();
        StartScreenController.sceneGenerator(stage, root, "MainScreen-view.fxml", event, "Main Screen");
    }

    @FXML
    void initialize() {
        assert goBackButton != null : "fx:id=\"goBackButton\" was not injected: check your FXML file 'LevelSelectionScreen-view.fxml'.";
        assert tilePane != null : "fx:id=\"tilePane\" was not injected: check your FXML file 'LevelSelectionScreen-view.fxml'.";
        initializeLevels();
    }

    //Fills tilePane with all the player's unlocked levels represented by an image and a label.
    public void initializeLevels(){
        for(Level level : LevelManager.getLevelDatabase()){
            if(LoginManager.getCurrentPlayer().getScoreMap().containsKey(level)){
                AnchorPane pane = new AnchorPane();

                ImageView levelIcon = new ImageView(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/sceneElements/levelIcon.png"))));
                //Changes levelIcon's size to fit in the pane.
                double ratio = Math.min(Screen.getPrimary().getBounds().getHeight() / levelIcon.getImage().getHeight(), Screen.getPrimary().getBounds().getWidth() / levelIcon.getImage().getWidth());
                levelIcon.setFitHeight(levelIcon.getImage().getHeight()*ratio*0.14);
                levelIcon.setFitWidth(levelIcon.getImage().getWidth()*ratio*0.14);
                //Creates a centered label that contains the levelNum
                Label levelLabel = new Label("Level " + level.getLevelNum());
                levelLabel.setPrefHeight(levelIcon.getFitHeight());
                levelLabel.setPrefWidth(levelIcon.getFitWidth());
                levelLabel.getStyleClass().add("centered-label");

                pane.getChildren().addAll(levelIcon, levelLabel);

                pane.setOnMouseClicked(getLevelListener()); //Click listener that sets the current level using the info from the pane that is clicked.
                tilePane.getChildren().add(pane);
            }
        }
    }



    public EventHandler<MouseEvent> getLevelListener() {
        return event -> {
            Label levelLabel = (Label)((AnchorPane) event.getSource()).getChildren().get(1);
            int level = Integer.parseInt(levelLabel.getText().split(" ")[1].trim());

            LevelManager.setCurrentLevel(LevelManager.getLevel(level));
            try {
                StartScreenController.sceneGenerator(stage, root, "LevelScreen-view.fxml", event, "Level Screen");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @FXML
    void openTutorialsScreen(MouseEvent event) throws IOException {
        StartScreenController.sceneGenerator(stage, root, "TutorialScreen-view.fxml", event, "Tutorial Screen");
    }
}
