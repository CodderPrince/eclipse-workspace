package com.gui;

import com.game.GameSettings;
import com.game.LoginManager;
import com.game.Music;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public class StartScreen extends Application {
    @Override
    public void start(Stage stage) {
        Parent root;

        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("StartScreen-view.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GameSettings gameSettings = SettingsScreenController.loadDefaultSettings("settings.txt");

        Scene scene = new Scene(root);
        stage.setTitle("Start Screen");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/profile/profileIconRed.png"))));

        if (gameSettings != null && gameSettings.isFullScreen()) {
            stage.setFullScreen(true);
        } else {
            stage.setFullScreen(false);
            stage.setMaximized(true);
        }


        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();

        LoginManager.initializeLoginManager("players.txt");
        
        new Music();
    }

    public static void main(String[] args) {
        launch();
    }
}