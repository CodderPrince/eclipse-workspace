package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HangmanGame1 extends Application {

    private String hiddenWord = "JAVA";
    private StringBuilder currentGuess;
    private int attemptsLeft = 6;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize the current guess
        currentGuess = new StringBuilder("_".repeat(hiddenWord.length()));

        // Create UI elements
        Label wordLabel = new Label("Word: " + currentGuess.toString());
        Label attemptsLabel = new Label("Attempts Left: " + attemptsLeft);
        TextField inputField = new TextField();
        inputField.setPromptText("Enter a letter");
        Button guessButton = new Button("Guess");
        Label messageLabel = new Label();

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

        // Create layout and add elements
        VBox layout = new VBox(10);
        layout.getChildren().addAll(wordLabel, attemptsLabel, inputField, guessButton, messageLabel);

        // Set up the stage
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setTitle("Hangman Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
