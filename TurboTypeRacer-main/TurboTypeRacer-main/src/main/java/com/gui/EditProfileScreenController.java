package com.gui;

import com.game.LoginManager;
import com.game.Music;
import com.game.Player;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class EditProfileScreenController {
    private final ArrayList<ImageView> iconList = new ArrayList<>();

    @FXML
    private TextField userNameTextField, phoneNumberField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView greenIcon, redIcon, blueIcon, purpleIcon, goBackButton;

    private String profileImage;

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


    //Method called when a player picks a profile icon.
    //Stores the icon on the profileImage attribute.
    //Adds a glowing effect on the chosen profile icon.
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

    //Returns the colour of the given profile icon.
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

    //Returns the profile icon that corresponds to the following imageUrl.
    public ImageView getIconImageView(String imageUrl){
        if (imageUrl.equals(redIcon.getImage().getUrl())) {
            return redIcon;
        } else if (imageUrl.equals(blueIcon.getImage().getUrl())) {
            return blueIcon;
        } else if (imageUrl.equals(greenIcon.getImage().getUrl())) {
            return greenIcon;
        } else if (imageUrl.equals(purpleIcon.getImage().getUrl())) {
            return purpleIcon;
        }

        return null;
    }

    @FXML
    void returnToMain(MouseEvent event) throws IOException {
        Music.playButtonSound();
        StartScreenController.sceneGenerator(stage, root, "MainScreen-view.fxml", event, "Main Screen");
    }

    //Used to update current player's info.
    @FXML
    void updateClicked(MouseEvent event) throws IOException {
        Music.playButtonSound();
        String name = userNameTextField.getText();
        String password = passwordField.getText();
        String phoneNumber = phoneNumberField.getText();

        if(profileImage!=null && !name.isEmpty() && !password.isEmpty() && !phoneNumber.isEmpty()){ //Checks if every field is filled.
            if(!LoginManager.playerExists(name) || name.equals(LoginManager.getCurrentPlayer().getName())){
                Player updatedPlayer = LoginManager.getCurrentPlayer();

                updatedPlayer.setName(name);
                updatedPlayer.setPassword(password);
                updatedPlayer.setPhoneNumber(phoneNumber);
                updatedPlayer.setImage(profileImage);

                LoginManager.savePlayerDatabase("players.txt");

                StartScreenController.sceneGenerator(stage, root, "MainScreen-view.fxml", event, "Main Screen");
            }
        }
    }

    @FXML
    void initialize() {
        assert blueIcon != null : "fx:id=\"blueIcon\" was not injected: check your FXML file 'EditProfileScreen-view.fxml'.";
        assert goBackButton != null : "fx:id=\"goBackButton\" was not injected: check your FXML file 'EditProfileScreen-view.fxml'.";
        assert greenIcon != null : "fx:id=\"greenIcon\" was not injected: check your FXML file 'EditProfileScreen-view.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'EditProfileScreen-view.fxml'.";
        assert phoneNumberField != null : "fx:id=\"phoneNumberField\" was not injected: check your FXML file 'EditProfileScreen-view.fxml'.";
        assert purpleIcon != null : "fx:id=\"purpleIcon\" was not injected: check your FXML file 'EditProfileScreen-view.fxml'.";
        assert redIcon != null : "fx:id=\"redIcon\" was not injected: check your FXML file 'EditProfileScreen-view.fxml'.";
        assert userNameTextField != null : "fx:id=\"userNameTextField\" was not injected: check your FXML file 'EditProfileScreen-view.fxml'.";

        iconList.add(redIcon);
        iconList.add(blueIcon);
        iconList.add(greenIcon);
        iconList.add(purpleIcon);

        initializeFields();
    }

    public void initializeFields(){
        userNameTextField.setText(LoginManager.getCurrentPlayer().getName());
        passwordField.setText(LoginManager.getCurrentPlayer().getPassword());
        phoneNumberField.setText(LoginManager.getCurrentPlayer().getPhoneNumber());
        profileImage = LoginManager.getCurrentPlayer().getProfileImage();
        ImageView selectedImage = getIconImageView(profileImage);
        selectedImage.setStyle("-fx-effect: dropShadow(three-pass-box, " + getIconColour(selectedImage) + ", 30, 0.5, 0, 0)");
    }

}
