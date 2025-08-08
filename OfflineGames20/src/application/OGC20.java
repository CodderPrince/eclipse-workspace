package application;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OGC20 extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Buttons for each game
    	Button btnTicTacToe = createStyledButton("Tic Tac Toe", "#8B4513", "#A0522D");
    	btnTicTacToe.setOnAction(e -> launchTicTacToe());


        Button btnSudokuSolver = createStyledButton("Sudoku Solver", "#32cd32", "#228b22");
        btnSudokuSolver.setOnAction(e -> launchSudokuSolver());

        Button btnSnakeGame = createStyledButton("Snake Game", "#1e90ff", "#00008b");
        btnSnakeGame.setOnAction(e -> launchSnakeGame());

        Button btnMinesweeper = createStyledButton("Minesweeper", "#ff69b4", "#c71585");
        btnMinesweeper.setOnAction(e -> launchMinesweeper());

        Button btnBrickBreaker = createStyledButton("Brick Breaker", "#ffa500", "#ff8c00");
        btnBrickBreaker.setOnAction(e -> launchBrickBreaker());

        Button btn2048Game = createStyledButton("2048 Game", "#8a2be2", "#4b0082");
        btn2048Game.setOnAction(e -> launch2048Game());

        Button btnFlappyBird = createStyledButton("Flappy Bird", "#ff6347", "#ff0000");
        btnFlappyBird.setOnAction(e -> launchFlappyBird());

        Button btnHangman = createStyledButton("Hangman Game", "#87ceeb", "#4682b4");
        btnHangman.setOnAction(e -> launchHangman());

        Button btnMemoryMatch = createStyledButton("Memory Match", "#843C0C", "#dc143c");
        btnMemoryMatch.setOnAction(e -> launchMemoryMatch());

        Button btnTowerOfHanoi = createStyledButton("Tower of Hanoi", "#00FFFF", "#008B8B");
        btnTowerOfHanoi.setOnAction(e -> launchTowerOfHanoi());


        // Left column buttons
        VBox leftColumn = new VBox(20);
        leftColumn.getChildren().addAll(btnTicTacToe, btnSudokuSolver, btnSnakeGame, btnMinesweeper, btnBrickBreaker);

        // Right column buttons
        VBox rightColumn = new VBox(20);
        rightColumn.getChildren().addAll(btn2048Game, btnFlappyBird, btnHangman, btnMemoryMatch, btnTowerOfHanoi);

        // Styling for columns
        leftColumn.setStyle("-fx-alignment: center;");
        rightColumn.setStyle("-fx-alignment: center;");

        // Layout with two columns
        HBox layout = new HBox(40);
        layout.getChildren().addAll(leftColumn, rightColumn);
        layout.setStyle("-fx-alignment: center; -fx-background-color: #2c3e50;");

        // Scene and Stage setup
        Scene scene = new Scene(layout, 610, 450);
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
        button.setFont(Font.font("Arial Rounded MT Bold", 25));
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

    private void launchBrickBreaker() {
        BrickBreaker brickBreakerApp = new BrickBreaker();
        Stage brickBreakerStage = new Stage();
        brickBreakerApp.launchGame(brickBreakerStage);
    }

    private void launch2048Game() {
        Game2048 game2048App = new Game2048();
        Stage game2048Stage = new Stage();
        game2048App.start(game2048Stage);
    }

    private void launchFlappyBird() {
        FlappyBird flappyBirdApp = new FlappyBird();
        Stage flappyBirdStage = new Stage();
        flappyBirdApp.start(flappyBirdStage);
    }

    private void launchHangman() {
        HangmanGame hangmanApp = new HangmanGame();
        Stage hangmanStage = new Stage();
        hangmanApp.start(hangmanStage);
    }

    private void launchMemoryMatch() {
    	MemoryMatchGame memoryMatchApp = new MemoryMatchGame();
        Stage memoryMatchStage = new Stage();
        memoryMatchApp.start(memoryMatchStage);
    }

    private void launchTowerOfHanoi() {
        TowerOfHanoi towerOfHanoiApp = new TowerOfHanoi();
        Stage towerOfHanoiStage = new Stage();
        towerOfHanoiApp.startTowerOfHanoi(towerOfHanoiStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
