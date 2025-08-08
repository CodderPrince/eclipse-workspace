package com.gui;

import com.game.Music;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class TutorialScreenController {
    private Stage stage;
    private Parent root;
    @FXML
    private ImageView leftArrow, rightArrow, tutorialInfo;
    private int numberOfPage = 1;
    @FXML
    void initialize() {
        assert leftArrow != null : "fx:id=\"leftArrow\" was not injected: check your FXML file 'TutorialScreen-view.fxml'.";
        assert rightArrow != null : "fx:id=\"rightArrow\" was not injected: check your FXML file 'TutorialScreen-view.fxml'.";
        assert tutorialInfo != null : "fx:id=\"tutorialInfo\" was not injected: check your FXML file 'TutorialScreen-view.fxml'.";
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
    void changePage(MouseEvent event) {
        Music.playButtonSound();

        if (event.getSource() == rightArrow && numberOfPage < 9)
            numberOfPage++;

        else if (event.getSource() == leftArrow && numberOfPage > 1)
            numberOfPage--;

        String page = "/assets/tutorial/";
        tutorialInfo.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream(page + numberOfPage + ".png"))));
        switch(numberOfPage) {
            case 1 -> leftArrow.setVisible(false);
            case 2 -> leftArrow.setVisible(true);
            case 8 -> rightArrow.setVisible(true);
            case 9 -> rightArrow.setVisible(false);
        }
    }

    @FXML
    void goBack(MouseEvent event) throws IOException {
        StartScreenController.sceneGenerator(stage, root, "LevelSelectionScreen-view.fxml", event, "LevelSelection Screen");
    }
}
