package application;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class OGC25 extends Application {

    private User currentUser;
    private Stage primaryStage;
    private Map<String, Integer> leaderboard = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showAccountCreationWindow();
    }

    private void showAccountCreationWindow() {
        // Layout for account creation
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(10);
        root.setHgap(10);

        Label nameLabel = new Label("Enter your name:");
        TextField nameField = new TextField();
        Label ageLabel = new Label("Enter your age:");
        TextField ageField = new TextField();
        Button createAccountButton = new Button("Create Account");
        Button leaderboardButton = new Button("Leaderboard");

        root.add(nameLabel, 0, 0);
        root.add(nameField, 1, 0);
        root.add(ageLabel, 0, 1);
        root.add(ageField, 1, 1);
        root.add(createAccountButton, 1, 2);
        root.add(leaderboardButton, 1, 3);

        // Button action to create the user account
        createAccountButton.setOnAction(e -> {
            String name = nameField.getText();
            String ageText = ageField.getText();
            if (name.isEmpty() || ageText.isEmpty()) {
                // Show error if fields are empty
                nameLabel.setText("Enter your name: (Required)");
                ageLabel.setText("Enter your age: (Required)");
                return;
            }

            try {
                int age = Integer.parseInt(ageText);
                currentUser = new User(name, age);
                showGameSelectionWindow(); // Proceed to game selection
            } catch (NumberFormatException ex) {
                ageLabel.setText("Enter your age: (Invalid number)");
            }
        });

        // Button action to show leaderboard
        leaderboardButton.setOnAction(e -> showLeaderboardWindow());

        Scene scene = new Scene(root, 300, 300);
        primaryStage.setTitle("Offline Games Collection - User Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showGameSelectionWindow() {
        // Buttons for each game
        Button btnTicTacToe = createStyledButton("Tic Tac Toe", "#8B4513", "#A0522D");
        btnTicTacToe.setOnAction(e -> {
            launchTicTacToe();
            updateLeaderboard("Tic Tac Toe", new Random().nextInt(500));
        });

        Button btnSudokuSolver = createStyledButton("Sudoku Solver", "#32cd32", "#228b22");
        btnSudokuSolver.setOnAction(e -> {
            launchSudokuSolver();
            updateLeaderboard("Sudoku Solver", new Random().nextInt(500));
        });

        Button btnSnakeGame = createStyledButton("Snake Game", "#1e90ff", "#00008b");
        btnSnakeGame.setOnAction(e -> {
            launchSnakeGame();
            updateLeaderboard("Snake Game", new Random().nextInt(500));
        });

        Button btnMinesweeper = createStyledButton("Minesweeper", "#8B0000", "#660000");
        btnMinesweeper.setOnAction(e -> {
            launchMinesweeper();
            updateLeaderboard("Minesweeper", new Random().nextInt(500));
        });

        Button btnBrickBreaker = createStyledButton("Brick Breaker", "#ffa500", "#ff8c00");
        btnBrickBreaker.setOnAction(e -> {
            launchBrickBreaker();
            updateLeaderboard("Brick Breaker", new Random().nextInt(500));
        });

        Button btn2048Game = createStyledButton("2048 Game", "#8a2be2", "#4b0082");
        btn2048Game.setOnAction(e -> {
            launch2048Game();
            updateLeaderboard("2048 Game", new Random().nextInt(500));
        });

        Button btnFlappyBird = createStyledButton("Flappy Bird", "#ff6347", "#ff0000");
        btnFlappyBird.setOnAction(e -> {
            launchFlappyBird();
            updateLeaderboard("Flappy Bird", new Random().nextInt(500));
        });

        Button btnHangman = createStyledButton("Hangman Game", "#87ceeb", "#4682b4");
        btnHangman.setOnAction(e -> {
            launchHangman();
            updateLeaderboard("Hangman", new Random().nextInt(500));
        });

        Button btnMemoryMatch = createStyledButton("Memory Match", "#843C0C", "#dc143c");
        btnMemoryMatch.setOnAction(e -> {
            launchMemoryMatch();
            updateLeaderboard("Memory Match", new Random().nextInt(500));
        });

        Button btnTowerOfHanoi = createStyledButton("Tower of Hanoi", "#00FFFF", "#008B8B");
        btnTowerOfHanoi.setOnAction(e -> {
            launchTowerOfHanoi();
            updateLeaderboard("Tower of Hanoi", new Random().nextInt(500));
        });

        Button btnBeanMachine = createStyledButton("Bean Machine", "#003300", "#004d00");
        btnBeanMachine.setOnAction(e -> {
            launchBeanMachine();
            updateLeaderboard("Bean Machine", new Random().nextInt(500));
        });

        Button btnNCS = createStyledButton("NCS", "#808080", "#696969");
        btnNCS.setOnAction(e -> {
            launchUBC();
            updateLeaderboard("NCS", new Random().nextInt(500));
        });

        Button btnPingPong = createStyledButton("Ping Pong Ball", "#ff69b4", "#c71585");
        btnPingPong.setOnAction(e -> {
            launchPingPong();
            updateLeaderboard("Ping Pong", new Random().nextInt(500));
        });

        Button btnRacingCar = createStyledButton("Racing Car", "#2F4F4F", "#1C1C1C");
        btnRacingCar.setOnAction(e -> {
            launchRacingCar();
            updateLeaderboard("Racing Car", new Random().nextInt(500));
        });

        Button backButton = createStyledButton("Back", "#FF4500", "#FF6347");
        backButton.setOnAction(e -> showAccountCreationWindow());

        // Left column buttons
        VBox leftColumn = new VBox(20);
        leftColumn.getChildren().addAll(btnTicTacToe, btnSudokuSolver, btnSnakeGame, btnMinesweeper, btnBrickBreaker, btnPingPong, btnBeanMachine);

        // Right column buttons
        VBox rightColumn = new VBox(20);
        rightColumn.getChildren().addAll(btn2048Game, btnFlappyBird, btnHangman, btnMemoryMatch, btnRacingCar, btnTowerOfHanoi, btnNCS);

        // Layout with two columns
        HBox buttonLayout = new HBox(40, leftColumn, rightColumn);
        buttonLayout.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, buttonLayout, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-alignment: center; -fx-background-color: #2c3e50;");

        // Scene and Stage setup
        Scene scene = new Scene(layout, 630, 650);
        scene.setFill(createGradientBackground());

        primaryStage.setTitle("Offline Games Collection - Game Selection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateLeaderboard(String game, int score) {
        leaderboard.put(game + " (" + currentUser.getName() + ")", score);
    }

    private void showLeaderboardWindow() {
        // Sort leaderboard by scores in descending order
        List<Map.Entry<String, Integer>> sortedLeaderboard = new ArrayList<>(leaderboard.entrySet());
        sortedLeaderboard.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Layout for leaderboard
        VBox leaderboardLayout = new VBox(10);
        leaderboardLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Leaderboard");
        titleLabel.setFont(Font.font("Arial Rounded MT Bold", 20));

        // Add scores to the layout
        for (Map.Entry<String, Integer> entry : sortedLeaderboard) {
            Label scoreLabel = new Label(entry.getKey() + ": " + entry.getValue());
            scoreLabel.setFont(Font.font("Arial", 16));
            leaderboardLayout.getChildren().add(scoreLabel);
        }

        Scene scene = new Scene(leaderboardLayout, 400, 500);
        Stage leaderboardStage = new Stage();
        leaderboardStage.setTitle("Leaderboard");
        leaderboardStage.setScene(scene);
        leaderboardStage.show();
    }

    private Button createStyledButton(String text, String startColor, String endColor) {
        Button button = new Button(text);

        // Set button size
        button.setPrefWidth(250); // Set preferred width
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
        snakeGame.setScoreCallback(score -> {
            updateLeaderboard("Snake Game", score);
            System.out.println("Snake Game Score: " + score); // For debugging
        });
        snakeGame.start(new Stage());
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

    private void launchBeanMachine() {
        BMF25 beanMachineApp = new BMF25();
        Stage beanMachineStage = new Stage();
        beanMachineApp.start(beanMachineStage);
    }

    private void launchUBC() {
        NumberConversionSystem ncsApp = new NumberConversionSystem();
        Stage ncsStage = new Stage();
        ncsApp.start(ncsStage);
    }

    private void launchPingPong() {
        PingPong pingPongApp = new PingPong();
        Stage pingPongStage = new Stage();
        pingPongApp.start(pingPongStage);
    }

    private void launchRacingCar() {
        RacingCar3 racingCarApp = new RacingCar3();
        Stage racingCarStage = new Stage();
        racingCarApp.start(racingCarStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
