package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    TextField inputField; // Input field for operations
    TextField resultField; // Display field for results
    int openParenthesesCount = 0; // Track the number of unclosed parentheses

    @Override
    public void start(Stage primaryStage) {
        // Set up the input field, disable editing, and style it
        inputField = new TextField();
        inputField.setEditable(false);
        inputField.setAlignment(Pos.BASELINE_RIGHT);
        inputField.setStyle("-fx-background-color: darkblue; -fx-text-fill: white;");
        inputField.setFont(Font.font(24));

        // Set up the result field, disable editing, and style it
        resultField = new TextField();
        resultField.setEditable(false);
        resultField.setAlignment(Pos.BASELINE_RIGHT);
        resultField.setStyle("-fx-background-color: lightgrey; -fx-text-fill: darkred;");
        resultField.setFont(Font.font(30)); // Increased font size for better visibility

        // Set up the layout grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20)); // Uniform padding
        grid.setHgap(10); // Horizontal gap between cells
        grid.setVgap(10); // Vertical gap between cells

        // Add fields to the grid
        grid.add(inputField, 0, 0, 4, 1);
        grid.add(resultField, 0, 1, 4, 1);

        // Button labels including the toggle for parentheses
        String[] buttonLabels = {
            "C", "D", "()", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "%", "0", ".", "="
        };

        int buttonIdx = 0; // Index for button labels
        for (int i = 2; i < 7; i++) { // Start at row 2 to leave space for text fields
            for (int j = 0; j < 4; j++) {
                String text = buttonLabels[buttonIdx++];
                Button button = new Button(text);
                button.setPrefWidth(60); // Preferred button width
                button.setPrefHeight(40); // Preferred button height
                button.setFont(Font.font(18)); // Font size for button
                styleButton(button, text); // Apply style based on the button type
                button.setOnAction(e -> handleButtonClick(text));
                if (text.isEmpty()) {
                    button.setDisable(true); // Disable empty buttons
                    button.setVisible(false);
                }
                grid.add(button, j, i); // Add button to grid
            }
        }

        // Set the scene size and add it to the stage
        Scene scene = new Scene(grid, 320, 480); // Adjust size to better fit the new style
        primaryStage.setTitle("Professional Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleButtonClick(String text) {
        switch (text) {
            case "C":
                inputField.setText("");
                resultField.setText("");
                openParenthesesCount = 0;
                break;
            case "D":
                if (!inputField.getText().isEmpty()) {
                    String currentText = inputField.getText();
                    char lastChar = currentText.charAt(currentText.length() - 1);
                    if (lastChar == '(') {
                        openParenthesesCount--;
                    } else if (lastChar == ')') {
                        openParenthesesCount++;
                    }
                    inputField.setText(currentText.substring(0, currentText.length() - 1));
                }
                break;
            case "()":
                if (shouldOpenParenthesis()) {
                    inputField.setText(inputField.getText() + "(");
                    openParenthesesCount++;
                } else if (openParenthesesCount > 0) {
                    inputField.setText(inputField.getText() + ")");
                    openParenthesesCount--;
                }
                break;
            case "=":
                calculateResult();
                break;
            default:
                inputField.setText(inputField.getText() + text);
        }
    }

    private boolean shouldOpenParenthesis() {
        String text = inputField.getText();
        return text.isEmpty() || text.endsWith("(") || isOperator(text.charAt(text.length() - 1));
    }

    private boolean isOperator(char ch) {
        return "+-×÷".indexOf(ch) != -1;
    }

    private void calculateResult() {
        String value = inputField.getText();
        value = value.replace('÷', '/').replace('×', '*');
        try {
            double result = eval(value);
            resultField.setText(String.valueOf(result));
        } catch (Exception e) {
            resultField.setText("Error");
        }
    }

    public double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }

    private void styleButton(Button button, String text) {
        if ("C".equals(text) || "D".equals(text)) {
            button.setStyle("-fx-base: #FF6347; -fx-text-fill: #ffffff;"); // Tomato color for clear and delete
        } else if ("÷".equals(text) || "×".equals(text) || "-".equals(text) || "+".equals(text) || "()".equals(text)) {
            button.setStyle("-fx-base: #4682B4; -fx-text-fill: #ffffff;"); // Steel blue for operators
        } else if ("=".equals(text)) {
            button.setStyle("-fx-base: #32CD32; -fx-text-fill: #ffffff;"); // Lime green for equals
        } else {
            button.setStyle("-fx-base: #F0F0F0; -fx-text-fill: #000000;"); // Light gray for numbers and decimal
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
