package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.util.Random;
import java.util.Stack;

public class Main extends Application {
    private String currentPlayer = "❌";
    private Button[][] buttons = new Button[3][3];
    private boolean isPlayerVsComputer = false; // Flag for game mode
    private Random random = new Random(); // For computer moves
    private Stack<int[]> moveHistory = new Stack<>(); // Stack to keep track of moves for undo functionality

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tic Tac Toe - Neon Style");
        showStartScreen(primaryStage);
    }

    // Start screen to choose game mode
    private void showStartScreen(Stage primaryStage) {
        Button pvpButton = new Button("Player vs Player");
        pvpButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        pvpButton.setOnAction(e -> startGame(primaryStage, false)); // Player vs Player mode

        Button pvcButton = new Button("Player vs Computer");
        pvcButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #2196F3; -fx-text-fill: white;");
        pvcButton.setOnAction(e -> startGame(primaryStage, true)); // Player vs Computer mode

        VBox menu = new VBox(10, new Label("Choose Game Mode"), pvpButton, pvcButton);
        menu.setAlignment(Pos.CENTER);

        Scene scene = new Scene(menu, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startGame(Stage primaryStage, boolean playerVsComputer) {
        isPlayerVsComputer = playerVsComputer;
        currentPlayer = "❌";

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: #000000;"); // Dark background for neon effect

        DropShadow neonEffectX = new DropShadow(20, Color.RED);
        DropShadow neonEffectO = new DropShadow(20, Color.CYAN);

        // Set up the 3x3 grid with neon-styled buttons
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button("");
                button.setMinSize(120, 120);
                button.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: #000000;");
                final int r = row, c = col;
                button.setOnAction(e -> handleButtonPress(button, r, c, neonEffectX, neonEffectO));
                buttons[row][col] = button;
                grid.add(button, col, row);
            }
        }

        // Add Undo button
        Button undoButton = new Button("Undo");
        undoButton.setAlignment(Pos.CENTER);//set this button in center
        undoButton.setStyle("-fx-font-size: 14px; -fx-background-color: #FF1744; -fx-text-fill: white;");
        undoButton.setOnAction(e -> undoLastMove());
        grid.add(undoButton, 1, 3); // Place it under the grid

        StackPane root = new StackPane(grid);
        Scene scene = new Scene(root, 450, 500); // Adjusted for undo button
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleButtonPress(Button button, int row, int col, DropShadow neonEffectX, DropShadow neonEffectO) {
        if (button.getText().isEmpty()) {
            button.setText(currentPlayer);
            moveHistory.push(new int[]{row, col}); // Store the move
            if (currentPlayer.equals("❌")) {
                button.setStyle("-fx-font-size: 40px; -fx-text-fill: #FF0000; -fx-font-weight: bold;"); // Red neon for X
                button.setEffect(neonEffectX);
            } else {
                button.setStyle("-fx-font-size: 40px; -fx-text-fill: #00FFFF; -fx-font-weight: bold;"); // Cyan neon for O
                button.setEffect(neonEffectO);
            }

            if (checkWin()) {
                showAlert("Player " + currentPlayer + " wins!");
                resetBoard();
            } else if (isBoardFull()) {
                showAlert("It's a draw!");
                resetBoard();
            } else {
                currentPlayer = currentPlayer.equals("❌") ? "⭕️" : "❌";
                if (isPlayerVsComputer && currentPlayer.equals("⭕️")) {
                    computerMove(neonEffectO);
                }
            }
        }
    }

    private void undoLastMove() {
        if (!moveHistory.isEmpty()) {
            int[] lastMove = moveHistory.pop();
            Button lastButton = buttons[lastMove[0]][lastMove[1]];
            lastButton.setText("");
            lastButton.setStyle("-fx-font-size: 40px; -fx-text-fill: #000000; -fx-font-weight: bold;");
            lastButton.setEffect(null); // Remove effect
            currentPlayer = currentPlayer.equals("❌") ? "⭕️" : "❌"; // Switch back the player
        }
    }

    private void computerMove(DropShadow neonEffectO) {
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (!buttons[row][col].getText().isEmpty());

        buttons[row][col].setText(currentPlayer);
        buttons[row][col].setStyle("-fx-font-size: 40px; -fx-text-fill: #00FFFF; -fx-font-weight: bold;");
        buttons[row][col].setEffect(neonEffectO);
        moveHistory.push(new int[]{row, col}); // Store the computer's move

        if (checkWin()) {
            showAlert("Computer wins!");
            resetBoard();
        } else if (isBoardFull()) {
            showAlert("It's a draw!");
            resetBoard();
        } else {
            currentPlayer = "❌";
        }
    }

    private boolean checkWin() {
        // Check rows and columns for a win
        for (int i = 0; i < 3; i++) {
            if (checkLineMatch(i, 0, i, 2) || checkLineMatch(0, i, 2, i)) {
                return true;
            }
        }
        // Check diagonals
        if (checkLineMatch(0, 0, 2, 2) || checkLineMatch(0, 2, 2, 0)) {
            return true;
        }
        return false;
    }

    private boolean checkLineMatch(int startRow, int startCol, int endRow, int endCol) {
        Button startButton = buttons[startRow][startCol];
        Button middleButton = buttons[(startRow + endRow) / 2][(startCol + endCol) / 2];
        Button endButton = buttons[endRow][endCol];
        return !startButton.getText().isEmpty() &&
               startButton.getText().equals(middleButton.getText()) &&
               middleButton.getText().equals(endButton.getText());
    }

    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setStyle("-fx-font-size: 40px; -fx-text-fill: #000000; -fx-font-weight: bold;");
                buttons[row][col].setEffect(null); // Reset effects
            }
        }
        currentPlayer = "❌";
        moveHistory.clear(); // Clear the move history
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF; -fx-background-color: #000000;");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
