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
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.util.Random;

public class Main extends Application {
    private String currentPlayer = "❌";
    private Button[][] buttons = new Button[3][3];
    private boolean isPlayerVsComputer = false; // Flag for game mode
    private Random random = new Random(); // For computer moves
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tic Tac Toe - Neon Style");
        showStartScreen(primaryStage);
    }
    
    // Start screen to choose game mode
    private void showStartScreen(Stage primaryStage) {
        Button pvpButton = new Button("Play Player vs Player");
        pvpButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        pvpButton.setOnAction(e -> startGame(primaryStage, false)); // Player vs Player mode
        
        Button pvcButton = new Button("Play Player vs Computer");
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
                button.setOnAction(e -> handleButtonPress(button, neonEffectX, neonEffectO));
                buttons[row][col] = button;
                grid.add(button, col, row);
            }
        }

        StackPane root = new StackPane(grid);
        Scene scene = new Scene(root, 450, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void handleButtonPress(Button button, DropShadow neonEffectX, DropShadow neonEffectO) {
        if (button.getText().isEmpty()) {
            button.setText(currentPlayer);
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
    
    private void computerMove(DropShadow neonEffectO) {
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (!buttons[row][col].getText().isEmpty());
        
        buttons[row][col].setText(currentPlayer);
        buttons[row][col].setStyle("-fx-font-size: 40px; -fx-text-fill: #00FFFF; -fx-font-weight: bold;");
        buttons[row][col].setEffect(neonEffectO);
        
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
        // Check rows for a win
        for (int row = 0; row < 3; row++) {
            if (!buttons[row][0].getText().isEmpty() &&
                buttons[row][0].getText().equals(buttons[row][1].getText()) &&
                buttons[row][1].getText().equals(buttons[row][2].getText())) {
                System.out.println("Row " + row + " win"); // Debug log
                highlightWinningLine(row, 0, row, 2); // Correctly highlight the row
                return true;
            }
        }

        // Check columns for a win
        for (int col = 0; col < 3; col++) {
            if (!buttons[0][col].getText().isEmpty() &&
                buttons[0][col].getText().equals(buttons[1][col].getText()) &&
                buttons[1][col].getText().equals(buttons[2][col].getText())) {
                System.out.println("Column " + col + " win"); // Debug log
                highlightWinningLine(0, col, 2, col); // Correctly highlight the column
                return true;
            }
        }

        // Check main diagonal for a win
        if (!buttons[0][0].getText().isEmpty() &&
            buttons[0][0].getText().equals(buttons[1][1].getText()) &&
            buttons[1][1].getText().equals(buttons[2][2].getText())) {
            System.out.println("Main diagonal win"); // Debug log
            highlightWinningLine(0, 0, 2, 2); // Correctly highlight the diagonal
            return true;
        }

        // Check anti-diagonal for a win
        if (!buttons[0][2].getText().isEmpty() &&
            buttons[0][2].getText().equals(buttons[1][1].getText()) &&
            buttons[1][1].getText().equals(buttons[2][0].getText())) {
            System.out.println("Anti-diagonal win"); // Debug log
            highlightWinningLine(0, 2, 2, 0); // Correctly highlight the anti-diagonal
            return true;
        }

        return false; // If no win condition is met
    }

    
    private void highlightWinningLine(int startRow, int startCol, int endRow, int endCol) {
        Line line = new Line();
        line.setStartX(startCol * 140 + 70); // Positioning adjustments
        line.setStartY(startRow * 140 + 70);
        line.setEndX(endCol * 140 + 70);
        line.setEndY(endRow * 140 + 70);
        line.setStrokeWidth(5);
        line.setStroke(Color.CYAN);
        line.setEffect(new DropShadow(20, Color.CYAN));
        
        StackPane root = (StackPane) buttons[0][0].getScene().getRoot();
        root.getChildren().add(line);
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
        StackPane root = (StackPane) buttons[0][0].getScene().getRoot();
        root.getChildren().removeIf(node -> node instanceof Line);
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
