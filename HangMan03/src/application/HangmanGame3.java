package application;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class HangmanGame3 extends Application {

    private String[] wordBank = {"PRINCE", "KINGDOM", "JUNGLE", "ADVENTURE", "TREASURE"};
    private String hiddenWord;
    private StringBuilder currentGuess;
    private int attemptsLeft = 6;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Select a random word from the word bank
        hiddenWord = selectRandomWord();

        // Create a new scene for the initial screen
        VBox initialLayout = new VBox(20);
        initialLayout.setAlignment(Pos.CENTER);
        initialLayout.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, null, 
                        new Stop(0, Color.DARKGREEN), 
                        new Stop(1, Color.LIGHTGREEN)),
                CornerRadii.EMPTY, null)));

        Label welcomeLabel = new Label("Welcome to Hangman Game!");
        welcomeLabel.setFont(Font.font("Arial", 30));
        welcomeLabel.setTextFill(Color.WHITE);

        Button startGameButton = createStyledButton("Start Game", "#1E90FF", "#87CEFA");
        startGameButton.setOnAction(e -> {
            primaryStage.setScene(createGameScene(primaryStage));
        });

        initialLayout.getChildren().addAll(welcomeLabel, startGameButton);
        Scene initialScene = new Scene(initialLayout, 600, 500);
        primaryStage.setScene(initialScene);
        primaryStage.setTitle("Hangman Game");
        primaryStage.show();
    }

    private Scene createGameScene(Stage primaryStage) {
        // Initialize the current guess
        currentGuess = new StringBuilder("_".repeat(hiddenWord.length()));

        // Create UI elements for the main game
        Label wordLabel = new Label("Word: " + currentGuess.toString());
        wordLabel.setFont(Font.font("Arial Rounded MT Bold", 30));
        wordLabel.setTextFill(Color.YELLOW);

        Label attemptsLabel = new Label("Attempts Left: " + attemptsLeft);
        attemptsLabel.setFont(Font.font("Arial Rounded MT Bold", 26));
        attemptsLabel.setTextFill(Color.WHITE);

        TextField inputField = new TextField();
        inputField.setPromptText("Enter a letter");
        inputField.setFont(Font.font("Arial Rounded MT Bold", 22));

        Button guessButton = createStyledButton("Guess", "#FF4500", "#FF8C00"); // Orange gradient

        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", 18));
        messageLabel.setTextFill(Color.WHITE);

        Button backButton = createStyledButton("Quit", "#9370DB", "#8A2BE2"); // Violet gradient

        // Back button action to quit the game
        backButton.setOnAction(event -> {
            primaryStage.close(); // Close the application
        });

        // Add action to the Guess button
        guessButton.setOnAction(event -> {
            String input = inputField.getText().toUpperCase();
            if (input.isEmpty() || input.length() != 1) {
                messageLabel.setText("Please enter a single letter.");
                return;
            }

            char guessedLetter = input.charAt(0);
            boolean correctGuess = false;

            // Check if the guessed letter is in the hidden word
            for (int i = 0; i < hiddenWord.length(); i++) {
                if (hiddenWord.charAt(i) == guessedLetter) {
                    currentGuess.setCharAt(i, guessedLetter);
                    correctGuess = true;
                }
            }

            // Update game state
            if (!correctGuess) {
                attemptsLeft--;
                messageLabel.setText("Incorrect guess!");
            } else {
                messageLabel.setText("Correct guess!");
            }

            // Update UI
            wordLabel.setText("Word: " + currentGuess.toString());
            attemptsLabel.setText("Attempts Left: " + attemptsLeft);
            inputField.clear();

            // Check for game over or win
            if (attemptsLeft == 0) {
                messageLabel.setText("Game Over! The word was: " + hiddenWord);
                guessButton.setDisable(true);
                inputField.setDisable(true);
            } else if (currentGuess.toString().equals(hiddenWord)) {
                messageLabel.setText("Congratulations! You've guessed the word: " + hiddenWord);
                guessButton.setDisable(true);
                inputField.setDisable(true);
            }
        });

        // Create layout and add elements for the game scene
        VBox gameLayout = new VBox(20);
        gameLayout.setAlignment(Pos.CENTER);
        gameLayout.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, null, 
                        new Stop(0, Color.DARKGREEN), 
                        new Stop(1, Color.CYAN)),
                CornerRadii.EMPTY, null)));
        gameLayout.getChildren().addAll(wordLabel, attemptsLabel, inputField, guessButton, backButton, messageLabel);

        return new Scene(gameLayout, 600, 500);
    }

    private String selectRandomWord() {
        Random random = new Random();
        return wordBank[random.nextInt(wordBank.length)];
    }

    private Button createStyledButton(String text, String startColor, String endColor) {
        Button button = new Button(text);

        // Set button size
        button.setPrefWidth(300);  // Set preferred width
        button.setPrefHeight(70); // Set preferred height

        // Styling
        button.setFont(Font.font("Arial", 24));
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
            scaleUp.setToX(1.3);
            scaleUp.setToY(1.3);
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
}
