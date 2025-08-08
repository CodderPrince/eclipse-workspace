package com.gui;

import com.game.LevelManager;
import com.game.LoginManager;
import com.game.Music;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class StartScreenController {
    private Stage stage;
    private Parent root;

    @FXML
    private ImageView loginButton, signUpButton, quitButton,  leaderboardBtn, settingsButton;

    @FXML
    void initialize() {
        assert leaderboardBtn != null : "fx:id=\"leaderboardBtn\" was not injected: check your FXML file 'StartScreen-view.fxml'.";
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'StartScreen-view.fxml'.";
        assert quitButton != null : "fx:id=\"quitButton\" was not injected: check your FXML file 'StartScreen-view.fxml'.";
        assert signUpButton != null : "fx:id=\"signUpButton\" was not injected: check your FXML file 'StartScreen-view.fxml'.";
        assert settingsButton != null : "fx:id=\"settingsButton\" was not injected: check your FXML file 'StartScreen-view.fxml'.";

        LevelManager.initializeLevels();
    }

    @FXML
    void animationPop(MouseEvent event) {
        ((ImageView)event.getSource()).setStyle("-fx-effect: dropShadow(gaussian, " + "#E34255" + ", 28, 0.7, 0, 0)");
    }
    @FXML
    void animationPopUp(MouseEvent event) {
        ((ImageView)event.getSource()).setStyle(null);
    }

    @FXML
    void mouseClicked(MouseEvent event) throws IOException {
        Music.playButtonSound();
        if (event.getSource() == loginButton)
            sceneGenerator(stage, root, "LoginScreen-view.fxml", event, "Login Screen");
        else if (event.getSource() == signUpButton)
            sceneGenerator(stage, root,"SignUpScreen-view.fxml", event, "SignUp Screen");
        else if (event.getSource() == leaderboardBtn)
            sceneGenerator(stage,root,"LeaderBoardScreen-view.fxml", event, "LeaderBoard Screen");
        else if (event.getSource() == settingsButton){
            StartScreenController.sceneGenerator(stage, root, "SettingsScreen-view.fxml", event, "Settings Screen");
        }
        else if (event.getSource() == quitButton){
            LoginManager.savePlayerDatabase("players.txt");
            System.exit(0);
        }
    }

    public static void sceneGenerator(Stage stage, Parent root, String name, Event event, String Player_Screen) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(StartScreenController.class.getResource(name)));

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);

        stage.setFullScreen(SettingsScreenController.getGameSettings().isFullScreen());
        if(Player_Screen.equals("Level Screen")) stage.setFullScreen(true);

        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setTitle(Player_Screen);
        stage.show();
    }

}
