package application;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OfflineGames extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Buttons for each game
        Button btnTicTacToe = createStyledButton("Tic Tac Toe", "#ff7f50", "#ff4500");
        btnTicTacToe.setOnAction(e -> launchTicTacToe());

        Button btnSudokuSolver = createStyledButton("Sudoku Solver", "#32cd32", "#228b22");
        btnSudokuSolver.setOnAction(e -> launchSudokuSolver());

        Button btnSnakeGame = createStyledButton("Snake Game", "#1e90ff", "#00008b");
        btnSnakeGame.setOnAction(e -> launchSnakeGame());

        Button btnMinesweeper = createStyledButton("Minesweeper", "#ff69b4", "#c71585");
        btnMinesweeper.setOnAction(e -> launchMinesweeper());

        // Layout with centered buttons
        VBox layout = new VBox(20);
        layout.getChildren().addAll(btnTicTacToe, btnSudokuSolver, btnSnakeGame, btnMinesweeper);
        layout.setStyle("-fx-alignment: center; -fx-background-color: #2c3e50;");

        // Scene and Stage setup
        Scene scene = new Scene(layout, 400, 400);
        scene.setFill(createGradientBackground());

        primaryStage.setTitle("Offline Games Collection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createStyledButton(String text, String startColor, String endColor) {
        Button button = new Button(text);

        // Set button size
        button.setPrefWidth(250);  // Set preferred width
        button.setPrefHeight(60); // Set preferred height

        // Styling
        button.setFont(Font.font("Arial", 20));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: linear-gradient(" + startColor + ", " + endColor + ");" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: white; -fx-border-width: 2;");

        // Drop Shadow Effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setColor(Color.GRAY);
        button.setEffect(shadow);

        // Button Hover Animation
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

    private LinearGradient createGradientBackground() {
        return new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#6a5acd")),
                new Stop(1, Color.web("#483d8b")));
    }

    private void launchTicTacToe() {
        TicTacToe ticTacToe = new TicTacToe();
        ticTacToe.start(new Stage());
    }

    private void launchSudokuSolver() {
        SudokuSolver sudokuSolverApp = new SudokuSolver();
        Stage sudokuStage = new Stage();
        sudokuSolverApp.startApp(sudokuStage);
    }

    private void launchSnakeGame() {
        SnakeGame snakeGame = new SnakeGame();
        Stage snakeStage = new Stage();
        snakeGame.start(snakeStage);
    }

    private void launchMinesweeper() {
        Minesweeper minesweeperApp = new Minesweeper();
        Stage minesweeperStage = new Stage();
        minesweeperApp.start(minesweeperStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
