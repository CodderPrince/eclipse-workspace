package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import java.util.Collections;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;

public class MemoryMatchGame extends Application {

    private static final int GRID_SIZE = 4; // 4x4 grid
    private Button[][] buttons = new Button[GRID_SIZE][GRID_SIZE];
    private String[][] values = new String[GRID_SIZE][GRID_SIZE];
    private Button firstSelected = null;
    private Button secondSelected = null;
    private boolean waiting = false;
    private long startTime;
    private Text timerText = new Text("Time: 0s");
    private String selectedCategory = "Cards";
    private String selectedMode = "Player vs Computer";
    private String difficultyLevel = "Easy";

    @Override
    public void start(Stage primaryStage) {
        showWelcomeScreen(primaryStage);
    }

    private void showWelcomeScreen(Stage primaryStage) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        Text welcomeText = new Text("Welcome to Memory Match Game");
        welcomeText.setFont(Font.font(20));

        ToggleGroup difficultyGroup = new ToggleGroup();
        RadioButton easyButton = new RadioButton("Easy");
        easyButton.setToggleGroup(difficultyGroup);
        easyButton.setSelected(true);
        RadioButton mediumButton = new RadioButton("Medium");
        mediumButton.setToggleGroup(difficultyGroup);
        RadioButton difficultButton = new RadioButton("Difficult");
        difficultButton.setToggleGroup(difficultyGroup);

        ToggleGroup categoryGroup = new ToggleGroup();
        RadioButton animalButton = new RadioButton("Animal");
        animalButton.setToggleGroup(categoryGroup);
        animalButton.setSelected(true);
        RadioButton flagButton = new RadioButton("Flag");
        flagButton.setToggleGroup(categoryGroup);
        RadioButton cardsButton = new RadioButton("Cards");
        cardsButton.setToggleGroup(categoryGroup);

        ToggleGroup modeGroup = new ToggleGroup();
        RadioButton pvpButton = new RadioButton("Player vs Player");
        pvpButton.setToggleGroup(modeGroup);
        pvpButton.setSelected(true);
        RadioButton pvcButton = new RadioButton("Player vs Computer");
        pvcButton.setToggleGroup(modeGroup);

        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> {
            difficultyLevel = ((RadioButton) difficultyGroup.getSelectedToggle()).getText();
            selectedCategory = ((RadioButton) categoryGroup.getSelectedToggle()).getText();
            selectedMode = ((RadioButton) modeGroup.getSelectedToggle()).getText();
            showGameScreen(primaryStage);
        });

        Button exitButton = new Button("Exit Game");
        exitButton.setOnAction(e -> primaryStage.close());

        VBox difficultyBox = new VBox(5, new Text("Choose Difficulty Level"), easyButton, mediumButton, difficultButton);
        VBox categoryBox = new VBox(5, new Text("Choose Category"), animalButton, flagButton, cardsButton);
        VBox modeBox = new VBox(5, new Text("Select Mode"), pvpButton, pvcButton);

        root.getChildren().addAll(welcomeText, difficultyBox, categoryBox, modeBox, startButton, exitButton);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Memory Match Game - Welcome");
        primaryStage.show();
    }

    private void showGameScreen(Stage primaryStage) {
        BorderPane root = new BorderPane();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        initializeValues();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Button button = new Button();
                button.setPrefSize(100, 100);
                button.setFont(Font.font(20));
                button.setStyle("-fx-background-color: lightblue;");

                final int r = row, c = col;
                button.setOnAction(e -> handleButtonClick(button, r, c));

                buttons[row][col] = button;
                grid.add(button, col, row);
            }
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showWelcomeScreen(primaryStage));

        timerText.setFont(Font.font(20));

        BorderPane topPane = new BorderPane();
        topPane.setLeft(backButton);
        topPane.setCenter(timerText);
        topPane.setStyle("-fx-padding: 10; -fx-background-color: lightgray;");

        root.setTop(topPane);
        root.setCenter(grid);

        Scene scene = new Scene(root, 500, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Memory Match Game");
        primaryStage.show();

        startTimer();
    }

    private void initializeValues() {
        ArrayList<String> cardValues = new ArrayList<>();

        for (int i = 1; i <= (GRID_SIZE * GRID_SIZE) / 2; i++) {
            cardValues.add(String.valueOf(i));
            cardValues.add(String.valueOf(i));
        }

        Collections.shuffle(cardValues);

        int index = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
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
        button.setStyle("-fx-background-color: white; -fx-border-color: black;");

        if (firstSelected == null) {
            firstSelected = button;
        } else {
            secondSelected = button;
            waiting = true;

            if (firstSelected.getText().equals(secondSelected.getText())) {
                firstSelected.setDisable(true);
                secondSelected.setDisable(true);
                resetSelection();
            } else {
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    javafx.application.Platform.runLater(() -> {
                        firstSelected.setText("");
                        firstSelected.setStyle("-fx-background-color: lightblue;");
                        secondSelected.setText("");
                        secondSelected.setStyle("-fx-background-color: lightblue;");
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
                timerText.setText("Time: " + elapsedTime + "s");
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
