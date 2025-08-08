package application;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import java.util.Collections;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class MemoryMatchGame extends Application {

    private int gridSize = 4; // Default grid size for Easy
    private Button[][] buttons;
    private String[][] values;
    private Button firstSelected = null;
    private Button secondSelected = null;
    private boolean waiting = false;
    private int matchedPairs = 0;
    private long startTime;
    private Text timerText = new Text("Time: 0 seconds");

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #87cefa, #4682b4);");

        Text difficultyText = new Text("Choose Difficulty Level");
        difficultyText.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        difficultyText.setFill(Color.WHITE);

        ComboBox<String> difficultySelector = new ComboBox<>();
        difficultySelector.getItems().addAll("Easy", "Medium", "Hard");
        difficultySelector.setValue("Easy");

        difficultySelector.setStyle(
                "-fx-font-size: 25px; " +
                "-fx-font-family: 'Arial'; " +
                "-fx-background-color: linear-gradient(#FFD700, #FFA500); " +
                "-fx-text-fill: #FFFFFF; " +
                "-fx-border-color: #FFFFFF; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10;");

        difficultySelector.setOnAction(e -> {
            String selected = difficultySelector.getValue();
            switch (selected) {
                case "Easy":
                    gridSize = 4;
                    break;
                case "Medium":
                    gridSize = 6;
                    break;
                case "Hard":
                    gridSize = 8;
                    break;
            }
        });

        Button runButton = createStyledButton("Run", "#32cd32", "#228b22");
        runButton.setOnAction(e -> startGame(primaryStage, difficultySelector.getValue()));

        Button exitButton = createStyledButton("Exit", "#ff6347", "#ff0000");
        exitButton.setOnAction(e -> primaryStage.close());

        root.getChildren().addAll(difficultyText, difficultySelector, runButton, exitButton);

        Scene scene = new Scene(root, 800, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Memory Match Game");
        primaryStage.show();
    }

    private void startGame(Stage primaryStage, String difficulty) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        switch (difficulty) {
            case "Easy":
                root.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #98FB98, #228B22);");
                break;
            case "Medium":
                root.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #FFD700, #FFA500);");
                break;
            case "Hard":
                root.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #FF6347, #FF4500);");
                break;
        }

        timerText.setFont(Font.font(24));
        timerText.setFill(Color.DARKBLUE);

        Button backButton = createStyledButton("Back", "#87ceeb", "#4682b4");
        backButton.setOnAction(e -> start(primaryStage));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);

        double buttonSize = 600.0 / gridSize; // Adjust button size based on grid size
        buttons = new Button[gridSize][gridSize];
        values = new String[gridSize][gridSize];
        initializeValues();

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Button button = new Button();
                button.setPrefSize(buttonSize, buttonSize);
                button.setFont(Font.font(24));
                button.setStyle("-fx-background-color: lightblue; -fx-border-color: darkblue; -fx-border-width: 2px;");

                final int r = row, c = col;
                button.setOnAction(e -> handleButtonClick(button, r, c));

                buttons[row][col] = button;
                grid.add(button, col, row);
            }
        }

        root.getChildren().addAll(timerText, grid, backButton);

        Scene scene = new Scene(root, 800, 900);
        primaryStage.setScene(scene);
        primaryStage.show();

        startTimer();
    }

    private Button createStyledButton(String text, String startColor, String endColor) {
        Button button = new Button(text);

        button.setPrefWidth(250);
        button.setPrefHeight(60);

        button.setFont(Font.font("Arial", 20));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: linear-gradient(" + startColor + ", " + endColor + ");" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: white; -fx-border-width: 2;");

        button.setOnMouseEntered(e -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
            scaleUp.setToX(1.2);
            scaleUp.setToY(1.2);
            scaleUp.play();
        });
        button.setOnMouseExited(e -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
            scaleDown.setToX(1);
            scaleDown.setToY(1);
            scaleDown.play();
        });

        return button;
    }

    private void initializeValues() {
        ArrayList<String> cardValues = new ArrayList<>();

        for (int i = 1; i <= (gridSize * gridSize) / 2; i++) {
            cardValues.add(String.valueOf(i));
            cardValues.add(String.valueOf(i));
        }

        Collections.shuffle(cardValues);

        int index = 0;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                values[row][col] = cardValues.get(index);
                index++;
            }
        }
    }

    private void handleButtonClick(Button button, int row, int col) {
        if (waiting || button.getText().length() > 0) {
            return;
        }

        button.setText(values[row][col]);
        button.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2px;");

        if (firstSelected == null) {
            firstSelected = button;
        } else {
            secondSelected = button;
            waiting = true;

            if (firstSelected.getText().equals(secondSelected.getText())) {
                firstSelected.setDisable(true);
                secondSelected.setDisable(true);
                matchedPairs++;
                resetSelection();

                if (matchedPairs == (gridSize * gridSize) / 2) {
                    showCongratulationsDialog();
                }
            } else {
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    javafx.application.Platform.runLater(() -> {
                        firstSelected.setText("");
                        firstSelected.setStyle("-fx-background-color: lightblue; -fx-border-color: darkblue; -fx-border-width: 2px;");
                        secondSelected.setText("");
                        secondSelected.setStyle("-fx-background-color: lightblue; -fx-border-color: darkblue; -fx-border-width: 2px;");
                        resetSelection();
                    });
                }).start();
            }
        }
    }

    private void resetSelection() {
        firstSelected = null;
        secondSelected = null;
        waiting = false;
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                timerText.setText("Time: " + elapsedTime + " seconds");

                if (matchedPairs == (gridSize * gridSize) / 2) {
                    stop();
                }
            }
        };
        timer.start();
    }

    private void showCongratulationsDialog() {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Congratulations!");
        alert.setHeaderText(null);
        alert.setContentText("You completed the game in " + elapsedTime + " seconds!");

        Text content = new Text("Congratulations!\nYou completed the game in " + elapsedTime + " seconds!");
        content.setFont(Font.font(24));
        content.setFill(Color.DARKGREEN);
        content.setTextAlignment(TextAlignment.CENTER);

        alert.getDialogPane().setContent(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
