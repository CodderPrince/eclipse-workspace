package com.gui;

import java.io.IOException;

import com.game.LoginManager;
import com.game.Music;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import static com.gui.StartScreenController.sceneGenerator;

public class MainScreenController {

    @FXML
    private ImageView goBackButton, leaderboardBtn, levelsBtn, profileBtn, settingsBtn;
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
    void mouseClicked(MouseEvent event) throws IOException {
        Music.playButtonSound();
        if (event.getSource() == leaderboardBtn)
            sceneGenerator(stage, root, "LeaderBoardScreen-view.fxml", event, "Login Screen");
        else if (event.getSource() == levelsBtn)
            sceneGenerator(stage, root,"LevelSelectionScreen-view.fxml", event, "SignUp Screen");
        else if (event.getSource() == profileBtn)
            sceneGenerator(stage,root,"ProfileScreen-view.fxml", event, "LeaderBoard Screen");
        else if (event.getSource() == settingsBtn){
            sceneGenerator(stage, root, "SettingsScreen-view.fxml", event, "Settings Screen");
        }
        else if (event.getSource() == goBackButton){
            LoginManager.setCurrentPlayer(null);
            SettingsScreenController.loadDefaultSettings("settings.txt");
            new Music();
            sceneGenerator(stage, root, "StartScreen-view.fxml", event, "Start Screen");
        }
    }

    @FXML
    void initialize() {
        assert goBackButton != null : "fx:id=\"goBackButton\" was not injected: check your FXML file 'MainScreen-view.fxml'.";
        assert leaderboardBtn != null : "fx:id=\"leaderboardBtn\" was not injected: check your FXML file 'MainScreen-view.fxml'.";
        assert levelsBtn != null : "fx:id=\"levelsBtn\" was not injected: check your FXML file 'MainScreen-view.fxml'.";
        assert profileBtn != null : "fx:id=\"profileBtn\" was not injected: check your FXML file 'MainScreen-view.fxml'.";
        assert settingsBtn != null : "fx:id=\"settingsBtn\" was not injected: check your FXML file 'MainScreen-view.fxml'.";
    }

}
