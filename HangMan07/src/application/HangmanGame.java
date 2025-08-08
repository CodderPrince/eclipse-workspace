package application;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HangmanGame extends Application {

    private Map<String, String[]> categoryWordBank = new HashMap<>();
    private String hiddenWord;
    private StringBuilder currentGuess;
    private int attemptsLeft;
    private String currentCategory = "Programming";

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        initializeCategories();
        resetGame();

        VBox initialLayout = new VBox(20);
        initialLayout.setAlignment(Pos.CENTER);
        initialLayout.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, null,
                        new Stop(0, Color.DARKGREEN),
                        new Stop(1, Color.LIGHTGREEN)),
                CornerRadii.EMPTY, null)));

        Label welcomeLabel = new Label("Welcome to Hangman Game!");
        welcomeLabel.setFont(Font.font("Arial Rounded MT Bold", 35));
        welcomeLabel.setTextFill(Color.YELLOW);
        
        Label selectCategory = new Label("Select Category");
        selectCategory.setFont(Font.font("Arial", 25));
        selectCategory.setTextFill(Color.WHITE);
        
        ComboBox<String> categoryDropdown = new ComboBox<>();
        categoryDropdown.getItems().addAll("Programming","IDE","Search Engine","CP Platform", "Bird", "Animal", "Flower", "Country", "District", "Brand");
        categoryDropdown.setValue("Programming");
        categoryDropdown.setOnAction(event -> currentCategory = categoryDropdown.getValue());

        // Set custom font size, color, and style using inline CSS
        categoryDropdown.setStyle(
                "-fx-font-size: 25px; " +   // Font size
                "-fx-font-family: 'Arial'; " + // Font family
                "-fx-background-color: linear-gradient(#FFD700, #FFA500); " + // Gradient background color
                "-fx-text-fill: #FFFFFF; " +  // Text color
                "-fx-border-color: #FFFFFF; " + // Border color
                "-fx-border-radius: 10; " + // Rounded borders
                "-fx-background-radius: 10;"   // Rounded background
        );


        Button startGameButton = createStyledButton("Start Game", "#1E90FF", "#87CEFA");
        startGameButton.setOnAction(e -> primaryStage.setScene(createGameScene(primaryStage)));

        // Back button to exit the game
        Button backButton = createStyledButton("Exit Game", "#FF6347", "#FF4500");
        backButton.setOnAction(e -> primaryStage.close());

        initialLayout.getChildren().addAll(welcomeLabel,selectCategory, categoryDropdown, startGameButton, backButton);
        Scene initialScene = new Scene(initialLayout, 600, 500);
        primaryStage.setScene(initialScene);
        primaryStage.setTitle("Hangman Game");
        primaryStage.show();
    }


    private Scene createGameScene(Stage primaryStage) {
        resetGame(); // Ensure a new word is selected based on the currentCategory.

        Label wordLabel = new Label("Word: " + currentGuess.toString());
        wordLabel.setFont(Font.font("Arial Rounded MT Bold", 40));
        wordLabel.setTextFill(Color.YELLOW);

        Label attemptsLabel = new Label("Attempts Left: " + attemptsLeft);
        attemptsLabel.setFont(Font.font("Arial Rounded MT Bold", 26));
        attemptsLabel.setTextFill(Color.WHITE);

        TextField inputField = new TextField();
        inputField.setPromptText("Enter a letter");
        inputField.setFont(Font.font("Arial Rounded MT Bold", 22));

        Button guessButton = createStyledButton("Guess", "#FF4500", "#FF8C00");
        Button playAgainButton = createStyledButton("Play Again", "#32CD32", "#7CFC00");
        Button backButton = createStyledButton("Back", "#9370DB", "#8A2BE2");

        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", 18));
        messageLabel.setTextFill(Color.WHITE);

     // Define a shared logic for handling guesses
        Runnable handleGuess = () -> {
            String input = inputField.getText().toUpperCase().trim();
            if (input.isEmpty() || input.length() != 1) {
                messageLabel.setText("Please enter a single letter.");
                messageLabel.setTextFill(Color.YELLOW); // Warn the user with yellow text
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

            // Update message and color based on the guess
            if (!correctGuess) {
                attemptsLeft--;
                messageLabel.setText("Incorrect guess!");
                messageLabel.setTextFill(Color.WHITE); // White for incorrect guesses
            } else {
                messageLabel.setText("Correct guess!");
                messageLabel.setTextFill(Color.GREEN); // Green for correct guesses
            }

            // Update the word and attempts left display
            wordLabel.setText("Word: " + currentGuess.toString());
            attemptsLabel.setText("Attempts Left: " + attemptsLeft);
            inputField.clear();

            // Check for game over or victory conditions
            if (attemptsLeft == 0) {
                // Display a game over dialogue
                Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
                gameOverAlert.setTitle("Game Over");
                gameOverAlert.setHeaderText(null);
                gameOverAlert.setContentText("Game Over! The word was: " + hiddenWord);
                gameOverAlert.showAndWait();

                messageLabel.setText("Game Over! The word was: " + hiddenWord);
                messageLabel.setTextFill(Color.WHITE); // Red text for game over
                guessButton.setDisable(true);
                inputField.setDisable(true);
            } else if (currentGuess.toString().equals(hiddenWord)) {
                // Display a success dialogue
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Congratulations!");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Congratulations! You've guessed the word: " + hiddenWord);
                successAlert.showAndWait();

                messageLabel.setText("Congratulations! You've guessed the word: " + hiddenWord);
                messageLabel.setTextFill(Color.GREEN); // Green text for success
                guessButton.setDisable(true);
                inputField.setDisable(true);
            }
        };

        // Set action for the "Guess" button
        guessButton.setOnAction(event -> handleGuess.run());

        // Set action for the Enter key in the text field
        inputField.setOnAction(event -> handleGuess.run());

        
        

        playAgainButton.setOnAction(event -> {
            resetGame();
            primaryStage.setScene(createGameScene(primaryStage));
        });

        backButton.setOnAction(event -> start(primaryStage));

        VBox gameLayout = new VBox(20);
        gameLayout.setAlignment(Pos.CENTER);
        gameLayout.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, null,
                        new Stop(0, Color.DARKBLUE),
                        new Stop(1, Color.CYAN)),
                CornerRadii.EMPTY, null)));
        gameLayout.getChildren().addAll(wordLabel, attemptsLabel, inputField, guessButton, playAgainButton, backButton, messageLabel);

        return new Scene(gameLayout, 600, 500);
    }
 

    private void resetGame() {
        hiddenWord = selectRandomWord();
        currentGuess = new StringBuilder("_".repeat(hiddenWord.length()));
        attemptsLeft = 6;
    }

    private String selectRandomWord() {
        Random random = new Random();
        String[] words = categoryWordBank.getOrDefault(currentCategory, new String[]{"UNKNOWN"});
        return words[random.nextInt(words.length)];
    }

    private void initializeCategories() {
        categoryWordBank.put("Programming", new String[]{"CSE", "JAVA", "PYTHON", "C++","DART","FLUTTER","JAVASCRIPT"});
        categoryWordBank.put("IDE", new String[]{"VS CODE", "ECLIPSE", "INTELLIJ", "CODEBLOCKS","PYCHARM","ANDROID STUDIO"});
        categoryWordBank.put("Search Engine", new String[]{"CHROME", "FIREFOX", "OPERA MINI", "BRAVE","MS EDGE"});
        categoryWordBank.put("CP Platform", new String[]{"CODEFORCES", "LEETCODE", "DIMIK OJ", "ICPC","HACKERRANK","CSES","BEECROWD"});
        categoryWordBank.put("Bird", new String[]{"SPARROW", "PEACOCK", "PIGEON", "EAGLE"});
        categoryWordBank.put("Animal", new String[]{"TIGER", "ELEPHANT", "KANGAROO", "GIRAFFE"});
        categoryWordBank.put("Flower", new String[]{"ROSE", "LILY", "TULIP", "SUNFLOWER"});
        categoryWordBank.put("Country", new String[]{"BANGLADESH", "CANADA", "AUSTRALIA", "GERMANY"});
        categoryWordBank.put("District", new String[]{"DHAKA", "CHITTAGONG", "SYLHET", "RAJSHAHI"});
        categoryWordBank.put("Brand", new String[]{"NIKE", "ADIDAS", "SONY", "APPLE"});
    }

    private Button createStyledButton(String text, String startColor, String endColor) {
        Button button = new Button(text);
        button.setPrefWidth(300);
        button.setPrefHeight(70);
        button.setFont(Font.font("Arial", 24));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: linear-gradient(" + startColor + ", " + endColor + ");" +
                "-fx-background-radius: 15;" +
                "-fx-border-radius: 15;" +
                "-fx-border-color: white; -fx-border-width: 2;");

        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setColor(Color.GRAY);
        button.setEffect(shadow);

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
