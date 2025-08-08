package com.gui;


import com.game.LoginManager;
import com.game.Music;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginScreenController {
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label promtLabel;
    @FXML
    private HBox promtHbox;
    @FXML
    private ImageView goBackButton, loginButton;
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
    public void returnToMain(MouseEvent event) throws IOException {
        Music.playButtonSound();
        StartScreenController.sceneGenerator(stage, root, "StartScreen-view.fxml", event, "Start Screen");
    }

    @FXML
    void initialize() {
        assert goBackButton != null : "fx:id=\"goBackButton\" was not injected: check your FXML file 'LoginScreen-view.fxml'.";
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'LoginScreen-view.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'LoginScreen-view.fxml'.";
        assert promtHbox != null : "fx:id=\"promtHbox\" was not injected: check your FXML file 'LoginScreen-view.fxml'.";
        assert userNameTextField != null : "fx:id=\"userNameTextField\" was not injected: check your FXML file 'LoginScreen-view.fxml'.";

    }

    //Loads the EditProfileScreen.
    public void editProfile(MouseEvent event) throws IOException {
        Music.playButtonSound();
        String name = userNameTextField.getText();
        String password = passwordField.getText();

        if(name!=null && password !=null){
            if(LoginManager.isValidLogin(name,password)){
                LoginManager.addCurrentPlayer(LoginManager.getPlayer(name));
                SettingsScreenController.setGameSettings(LoginManager.getCurrentPlayer().getGameSettings());
                new Music();
                StartScreenController.sceneGenerator(stage, root, "EditProfileScreen-view.fxml", event, "Edit Profile Screen");
            }
        }
    }

    public void textFieldClicked(MouseEvent event) {
        promtLabel.setText("");
    }

    //Loads the MainScreen.
    public void loginClicked(MouseEvent event) throws IOException {
        Music.playButtonSound();
        String name = userNameTextField.getText();
        String password = passwordField.getText();

        if(!name.isEmpty()){
            if(LoginManager.isValidLogin(name,password)){
                LoginManager.addCurrentPlayer(LoginManager.getPlayer(name));
                SettingsScreenController.setGameSettings(LoginManager.getCurrentPlayer().getGameSettings());
                new Music();
                StartScreenController.sceneGenerator(stage, root, "MainScreen-view.fxml", event, "Main Screen");
            } else {
                promtLabel.setText("User doesn't exist!!");
            }
        } else {
            promtLabel.setText("Must fill the required fields..!!");
        }
    }
}
