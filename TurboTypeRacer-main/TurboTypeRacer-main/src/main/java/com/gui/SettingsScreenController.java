package com.gui;

import com.game.GameSettings;
import com.game.LoginManager;
import com.game.Music;
import com.game.Player;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

public class SettingsScreenController {
    @FXML
    private ImageView goBackButton;
    @FXML
    private CheckBox fullscreenCheckBox, musicCheckBox, soundEffectsCheckBox;
    private static GameSettings gameSettings;

    private Stage stage;
    private Parent root;

    @FXML
    void animationPop(MouseEvent event) {
        ((Node)event.getSource()).setStyle("-fx-effect: dropShadow(gaussian, " + "#E34255" + ", 28, 0.7, 0, 0)");
    }
    @FXML
    void animationPopUp(MouseEvent event) {
        ((Node)event.getSource()).setStyle(null);
    }

    @FXML
    void returnToMain(MouseEvent event) throws IOException {
        Music.playButtonSound();
        if(LoginManager.getCurrentPlayer()!=null){
            StartScreenController.sceneGenerator(stage,root,"MainScreen-view.fxml", event, "Main Screen");
        } else {
            gameSettings = loadDefaultSettings("settings.txt");
            initializeSettings();
            StartScreenController.sceneGenerator(stage,root,"StartScreen-view.fxml", event, "Start Screen");
        }
    }

    @FXML
    void initialize() {
        assert fullscreenCheckBox != null : "fx:id=\"fullscreenCheckBox\" was not injected: check your FXML file 'SettingsScreen-view.fxml'.";
        assert goBackButton != null : "fx:id=\"goBackButton\" was not injected: check your FXML file 'SettingsScreen-view.fxml'.";
        assert musicCheckBox != null : "fx:id=\"musicCheckBox\" was not injected: check your FXML file 'SettingsScreen-view.fxml'.";
        assert soundEffectsCheckBox != null : "fx:id=\"soundEffectsCheckBox\" was not injected: check your FXML file 'SettingsScreen-view.fxml'.";

        initializeSettings();
    }

    // Sets the gameSettings attribute based on the current player's preferences,
    // or defaults to the application's default settings if no player preferences are available.
    public void initializeSettings(){
        fullscreenCheckBox.setSelected(gameSettings.isFullScreen());
        musicCheckBox.setSelected(gameSettings.isMusicSound());
        soundEffectsCheckBox.setSelected(gameSettings.isSoundEfectsSound());
    }

    //Updates settings
    public void saveSettingsClicked(MouseEvent event) throws IOException {
        gameSettings.setFullScreen(fullscreenCheckBox.isSelected());
        gameSettings.setMusicSound(musicCheckBox.isSelected());
        gameSettings.setSoundEfectsSound(soundEffectsCheckBox.isSelected());

        Player currentPlayer = LoginManager.getCurrentPlayer();
        if (currentPlayer != null){
            currentPlayer.setSettings(gameSettings);
            Music.changeSong(Music.getStartScreenSong());
            StartScreenController.sceneGenerator(stage, root, "MainScreen-view.fxml", event, "Main Screen");
        } else {
            saveSettings("settings.txt", gameSettings);
            Music.changeSong(Music.getStartScreenSong());
            StartScreenController.sceneGenerator(stage, root, "StartScreen-view.fxml", event, "Start Screen");
        }
    }

    //Imports default settings from settingsFile.txt
    public static GameSettings loadDefaultSettings(String fileName) {
        String path = Objects.requireNonNull(StartScreen.class.getResource("/settings/")).getPath()+fileName;

        File settingsFile = new File(path);
        // Checks if a file named after the parameter "fileName" exists in the settings directory.
        if(!settingsFile.exists()){
            try{
                settingsFile.createNewFile(); //Creates file if it doesn't exist.
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        if(settingsFile.length()!=0){
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
                gameSettings = (GameSettings) in.readObject(); //imports the stored settings.
                return gameSettings;
            } catch (IOException | ClassNotFoundException e ) {
                e.printStackTrace();
            }
        } else {
            gameSettings = new GameSettings(true, true, true);
            return gameSettings;
        }
        return null;
    }

    //Stores default settings in settingsFile.txt.
    public static void saveSettings(String fileName, GameSettings gameSettings){
        Music.playButtonSound();
        String path = Objects.requireNonNull(StartScreen.class.getResource("/settings/" + fileName)).getPath();
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameSettings);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameSettings getGameSettings(){
        return SettingsScreenController.gameSettings;
    }

    public static void setGameSettings(GameSettings gameSettings){
        SettingsScreenController.gameSettings = gameSettings;
    }
}
