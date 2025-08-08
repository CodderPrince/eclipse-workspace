package com.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import com.game.*;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ProfileScreenController {

    @FXML
    private ImageView profileImage, goBackButton, updateBtn, upArrowBtn, downArrowBtn, garageVehicleSelectionImageView;

    @FXML
    private TextField userNameTextField, phoneNumberField, passwordField;

    @FXML
    private ListView<String> statsList;

    private Stage stage;
    private Parent root;
    private static int numberOfCar = 1;

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
    void updateClicked(MouseEvent event) throws IOException {
        Music.playButtonSound();
        StartScreenController.sceneGenerator(stage, root, "EditProfileScreen-view.fxml", event, "Edit Profile Screen");
    }

    private void initializeStatsList() {
        Player currentPlayer = LoginManager.getCurrentPlayer();
        statsList.getItems().clear();
        for(Level level : currentPlayer.getScoreMap().keySet()) {
            statsList.getItems().add("Level " + level.getLevelNum() + ": Total wins: " + currentPlayer.getScoreMap().get(level));
        }
    }

    //Method used when player is selecting a car
    @FXML
    void changeCar(MouseEvent event) {
        Music.playButtonSound();
        Player currentPlayer = LoginManager.getCurrentPlayer();
        ArrayList<Vehicle> garage = currentPlayer.getGarage();
        int currentPlayerAvailableCarsAmount = LoginManager.getCurrentPlayer().getGarage().size();

        if (event.getSource() == downArrowBtn)
            numberOfCar++;

        else if (event.getSource() == upArrowBtn)
            numberOfCar--;

        String showingCar = garage.get(numberOfCar-1).getImageUrl(); //Loads next cars image
        garageVehicleSelectionImageView.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream(showingCar))));
        currentPlayer.setSelectedCar(garage.get(numberOfCar-1)); //Stores the selected car on the player

        //Handles the visibility of the arrow buttons
        initializeArrows(currentPlayerAvailableCarsAmount);
    }

    @FXML
    void initialize() {
        assert downArrowBtn != null : "fx:id=\"downArrowBtn\" was not injected: check your FXML file 'ProfileScreen-view.fxml'.";
        assert garageVehicleSelectionImageView != null : "fx:id=\"garageVehicleSelectionImageView\" was not injected: check your FXML file 'ProfileScreen-view.fxml'.";
        assert goBackButton != null : "fx:id=\"goBackButton\" was not injected: check your FXML file 'ProfileScreen-view.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'ProfileScreen-view.fxml'.";
        assert phoneNumberField != null : "fx:id=\"phoneNumberField\" was not injected: check your FXML file 'ProfileScreen-view.fxml'.";
        assert profileImage != null : "fx:id=\"profileImage\" was not injected: check your FXML file 'ProfileScreen-view.fxml'.";
        assert statsList != null : "fx:id=\"statsList\" was not injected: check your FXML file 'ProfileScreen-view.fxml'.";
        assert upArrowBtn != null : "fx:id=\"upArrowBtn\" was not injected: check your FXML file 'ProfileScreen-view.fxml'.";
        assert updateBtn != null : "fx:id=\"updateBtn\" was not injected: check your FXML file 'ProfileScreen-view.fxml'.";
        assert userNameTextField != null : "fx:id=\"userNameTextField\" was not injected: check your FXML file 'ProfileScreen-view.fxml'.";

        initializeFields();
        initializeStatsList();
    }

    public void initializeArrows(int currentPlayerAvailableCars){
        if(currentPlayerAvailableCars==1) {downArrowBtn.setVisible(false); upArrowBtn.setVisible(false);}
        else if (numberOfCar == 1) {downArrowBtn.setVisible(true); upArrowBtn.setVisible(false);}
        else if (numberOfCar > 1 && numberOfCar < currentPlayerAvailableCars) {downArrowBtn.setVisible(true); upArrowBtn.setVisible(true);}
        else if (numberOfCar == currentPlayerAvailableCars) {downArrowBtn.setVisible(false); upArrowBtn.setVisible(true);}
    }
    public void initializeFields(){
        initializeArrows(LoginManager.getCurrentPlayer().getGarage().size());
        garageVehicleSelectionImageView.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream(LoginManager.getCurrentPlayer().getSelectedCar().getImageUrl()))));
        userNameTextField.setText(LoginManager.getCurrentPlayer().getName());
        passwordField.setText(LoginManager.getCurrentPlayer().getPassword());
        phoneNumberField.setText(LoginManager.getCurrentPlayer().getPhoneNumber());
        String profileImageUrl = LoginManager.getCurrentPlayer().getProfileImage();
        profileImage.setImage((new Image(profileImageUrl)));
    }

}
