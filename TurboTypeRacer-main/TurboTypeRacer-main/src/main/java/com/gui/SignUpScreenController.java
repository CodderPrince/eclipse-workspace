package com.gui;

import com.game.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class SignUpScreenController {
    @FXML
    private TextField userNameTextField, phoneNumberField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ImageView goBackButton, loginButton;
    @FXML
    private ImageView greenIcon, redIcon, blueIcon, purpleIcon;
    private final ArrayList<ImageView> iconList = new ArrayList<>();
    @FXML
    private Label promtLabel;
    private String profileImage;
    private Stage stage;
    private Parent root;

    public void textFieldClicked() {
        promtLabel.setText("");
    }

    @FXML
    void returnToMain(MouseEvent event) throws IOException {
        Music.playButtonSound();
        StartScreenController.sceneGenerator(stage, root, "StartScreen-view.fxml", event, "Start Screen");

    }

    @FXML
    void chooseImage(MouseEvent event) {
        Music.playButtonSound();
        ImageView selectedImage = (ImageView) event.getSource();
        for(ImageView image : iconList){
            if(image!=selectedImage){
                image.setStyle(null);
            }
        }
        selectedImage.setStyle("-fx-effect: dropShadow(three-pass-box, " + getIconColour(selectedImage) + ", 30, 0.5, 0, 0)");
        profileImage = selectedImage.getImage().getUrl();

    }

    public String getIconColour(ImageView image){
        if (image.equals(redIcon)) {
            return "red";
        } else if (image.equals(blueIcon)) {
            return "blue";
        } else if (image.equals(greenIcon)) {
            return "green";
        } else if (image.equals(purpleIcon)) {
            return "purple";
        }

        return "black";
    }

    @FXML
    void animationPop(MouseEvent event) {
        ((Node)event.getSource()).setStyle("-fx-effect: dropShadow(gaussian, " + "#E34255" + ", 28, 0.7, 0, 0)");
    }
    @FXML
    void animationPopUp(MouseEvent event) {
        ((Node)event.getSource()).setStyle(null);
    }

    @FXML
    void initialize() {
        assert goBackButton != null : "fx:id=\"goBackButton\" was not injected: check your FXML file 'SignUpScreen-view.fxml'.";
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'SignUpScreen-view.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'SignUpScreen-view.fxml'.";
        assert phoneNumberField != null : "fx:id=\"phoneNumberField\" was not injected: check your FXML file 'SignUpScreen-view.fxml'.";
        assert userNameTextField != null : "fx:id=\"userNameTextField\" was not injected: check your FXML file 'SignUpScreen-view.fxml'.";
        assert blueIcon != null : "fx:id=\"blueIcon\" was not injected: check your FXML file 'SignUpScreen-view.fxml'.";
        assert redIcon != null : "fx:id=\"blueIcon\" was not injected: check your FXML file 'SignUpScreen-view.fxml'.";
        assert greenIcon != null : "fx:id=\"blueIcon\" was not injected: check your FXML file 'SignUpScreen-view.fxml'.";
        assert purpleIcon != null : "fx:id=\"blueIcon\" was not injected: check your FXML file 'SignUpScreen-view.fxml'.";
        iconList.add(redIcon);
        iconList.add(blueIcon);
        iconList.add(greenIcon);
        iconList.add(purpleIcon);

    }

    //Creates new player and loads the MainScreen.
    public void signUpClicked(MouseEvent event) throws IOException {
        Music.playButtonSound();
        String name = userNameTextField.getText();
        String password = passwordField.getText();
        String phoneNumber = phoneNumberField.getText();

        if(profileImage!=null && !name.isEmpty() && !password.isEmpty() && !phoneNumber.isEmpty()){ //Checks if all the fields are filled
            if(!LoginManager.playerExists(name)){

                ArrayList<Level> levelsList = new ArrayList<>();
                levelsList.add(LevelManager.getLevel(1));

                Player tempPlayer = new Player(name, password, phoneNumber, profileImage, levelsList);
                LoginManager.addUser(tempPlayer);

                LoginManager.savePlayerDatabase("players.txt");
                LoginManager.setCurrentPlayer(tempPlayer);

                SettingsScreenController.setGameSettings(LoginManager.getCurrentPlayer().getGameSettings());
                StartScreenController.sceneGenerator(stage, root,"MainScreen-view.fxml", event, "Main Screen" );
            } else
                promtLabel.setText("User already exists!!");
        }
        else if (name.isEmpty() || password.isEmpty() || phoneNumber.isEmpty())
            promtLabel.setText("Please fill all the fields");
        else
            promtLabel.setText("Choose a profile icon");

    }

}
