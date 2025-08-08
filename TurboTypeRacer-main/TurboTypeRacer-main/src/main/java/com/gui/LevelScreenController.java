package com.gui;

import com.game.*;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class LevelScreenController {
    @FXML
    private ImageView result, playerCar, computerCar;
    @FXML
    private BorderPane borderPane;
    @FXML
    private AnchorPane filter;
    @FXML
    private Label wordLabel;
    @FXML
    private VBox endBox,vBox;

    private Stage stage;
    private Parent root;
    private ArrayList<String> words = new ArrayList<>();
    private int lives = 3;
    private int wordAmount;
    private double xPlayerCoordinate = 0, xComputerCoordinate = 0,  yPlayerCoordinate = 0, yComputerCoordinate = 0;

    @FXML
    void animationPop(MouseEvent event) {
        ((Node)event.getSource()).setStyle("-fx-effect: dropShadow(gaussian, " + "#E34255" + ", 28, 0.7, 0, 0)");
    }
    @FXML
    void animationPopUp(MouseEvent event) {
        ((Node)event.getSource()).setStyle(null);
    }

    @FXML
    void goBack(MouseEvent event) throws IOException {
        Music.playButtonSound();
        Music.changeSong(Music.getStartScreenSong());
        LoginManager.savePlayerDatabase("players.txt");
        StartScreenController.sceneGenerator(stage,root,"LevelSelectionScreen-view.fxml", event, "Levels Screen");
    }

    @FXML
    void initialize() {
        Music.changeSong(Music.getGameScreenSong());
        assert borderPane != null : "fx:id=\"anchorPane\" was not injected: check your FXML file 'LevelScreen-view.fxml'.";
        assert computerCar != null : "fx:id=\"computerCar\" was not injected: check your FXML file 'LevelScreen-view.fxml'.";
        assert endBox != null : "fx:id=\"endBox\" was not injected: check your FXML file 'LevelScreen-view.fxml'.";
        assert vBox != null : "fx:id=\"hBox\" was not injected: check your FXML file 'LevelScreen-view.fxml'.";
        assert playerCar != null : "fx:id=\"playerCar\" was not injected: check your FXML file 'LevelScreen-view.fxml'.";
        assert result != null : "fx:id=\"result\" was not injected: check your FXML file 'LevelScreen-view.fxml'.";
        assert wordLabel != null : "fx:id=\"wordLabel\" was not injected: check your FXML file 'LevelScreen-view.fxml'.";
        assert filter != null : "fx:id=\"filter\" was not injected: check your FXML file 'LevelScreen-view.fxml'.";

        borderPane.getStyleClass().add("firstMap");

        words.addAll(LevelManager.getCurrentLevel().getWordsList());
        wordAmount = words.size();
        setCarImages();
        wordLabel.setText(words.get(0));


     }

    public void setCarImages(){
        playerCar.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream(LoginManager.getCurrentPlayer().getSelectedCar().getImageUrl()))));

        int levelNum = LevelManager.getCurrentLevel().getLevelNum();
        int carNum = (int)Math.ceil(levelNum/4.0);

        if (carNum > 5)
            carNum = 5;
        computerCar.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/vehicles/car"+carNum+".gif"))));
    }



    @FXML
    void checkText(KeyEvent event) {
        if((event.getCode() == KeyCode.SPACE || event.getCode() == KeyCode.ENTER) && !endBox.isVisible()){
            wordLabel.setStyle("-fx-text-fill: black");
            TextField field = (TextField) event.getSource();
            //Checks if the typed word was correct
            if(field.getText().trim().equals(wordLabel.getText())){
                Music.playPopSound();
                carAnimation(playerCar,1200, 670, xPlayerCoordinate, yPlayerCoordinate, "player");
                words.remove(0);
                //Checks if there are no words left.
                if(words.isEmpty()){
                    playerWon(field);
                } else {
                    wordLabel.setText(words.get(0));
                    field.setText("");
                }
            } else {
                wordLabel.setStyle("-fx-text-fill: red");
                field.setText("");

                if(xComputerCoordinate<xPlayerCoordinate) {
                    carAnimation(computerCar, 1200, 670, xComputerCoordinate, yComputerCoordinate, "computer");
                } else {
                    carAnimation(computerCar, 1200, 670, xPlayerCoordinate, yPlayerCoordinate, "computer");
                }

                //Removes a heart icon
                lives--;
                vBox.getChildren().get(lives).setVisible(false);

                //If there are no hearts left makes visible the Losing pane.
                if(lives==0){
                    Music.playLoseSound();
                    field.setEditable(false);
                    endBox.setVisible(true);
                    result.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/sceneElements/youLoseTitle.png"))));
                }
            }
        }
    }

    public void playerWon(TextField field){
        Music.playWinSound();
        Player currentPlayer = LoginManager.getCurrentPlayer();
        Level currentLevel = LevelManager.getCurrentLevel();

        //Increases players score.
        int playerScore = currentPlayer.getScore(currentLevel);
        LoginManager.getCurrentPlayer().setLevelAndScore(currentLevel, playerScore+1);

        //Adds new level and car to the player.
        Level nextLevel = LevelManager.getLevel(LevelManager.getCurrentLevel().getLevelNum()+1);
        if(nextLevel!=null && !currentPlayer.getScoreMap().containsKey(nextLevel)){
            currentPlayer.setLevelAndScore(nextLevel, 0);
            int newCar = (int)Math.ceil((nextLevel.getLevelNum())/5.0);
            if(currentLevel.getLevelNum()%5==0 && newCar <= 5)
                currentPlayer.getGarage().add(new Vehicle("Car"+newCar, "/assets/vehicles/car"+newCar+".gif"));
        }
        //Makes visible the Victory pane.
        endBox.setVisible(true);
        field.setEditable(false);
    }

    public void carAnimation(ImageView car, float xEndingPosition, float yEndingPosition, double xCoordinate, double yCoordinate, String owner){
        TranslateTransition translate = new TranslateTransition();

        translate.setToX(xCoordinate - (xEndingPosition/wordAmount));
        xCoordinate -= (xEndingPosition/wordAmount);


        translate.setToY(yCoordinate - (yEndingPosition/wordAmount));
        yCoordinate -= (yEndingPosition/wordAmount);

        translate.setDuration(Duration.millis(1000));
        translate.setAutoReverse(true);
        translate.setNode(car);
        translate.play();

        if(owner.equals("player")){
            xPlayerCoordinate = xCoordinate;
            yPlayerCoordinate = yCoordinate;
        } else {
            xComputerCoordinate = xCoordinate;
            yComputerCoordinate = yCoordinate;
        }
    }
}
