package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Number Conversion System");

        // Main layout
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20, 20, 20, 20));
        mainLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Number Conversion System");

        // Custom Base Button
        Button customBaseButton = new Button("Use Custom Base");
        customBaseButton.setOnAction(e -> openCustomBaseWindow(primaryStage));

        // Default Base Button
        Button defaultBaseButton = new Button("Use Default Base");
        defaultBaseButton.setOnAction(e -> openDefaultBaseWindow(primaryStage));

        mainLayout.getChildren().addAll(titleLabel, customBaseButton, defaultBaseButton);

        // Main scene
        Scene mainScene = new Scene(mainLayout, 400, 300);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void openCustomBaseWindow(Stage primaryStage) {
        VBox customLayout = new VBox(15);
        customLayout.setPadding(new Insets(20));
        customLayout.setAlignment(Pos.CENTER);

        Label baseLabel = new Label("Enter Your Base:");
        TextField baseInput = new TextField();
        baseInput.setPromptText("Enter base (e.g., 2 for Binary, 8 for Octal)");

        Label numberLabel1 = new Label("Enter First Base Number:");
        TextField numberInput1 = new TextField();
        numberInput1.setPromptText("Enter first base number");

        Label numberLabel2 = new Label("Enter Second Base Number:");
        TextField numberInput2 = new TextField();
        numberInput2.setPromptText("Enter second base number");

        ComboBox<String> targetBaseBox = new ComboBox<>();
        targetBaseBox.getItems().addAll("Decimal", "Binary", "Octal", "Hexadecimal");
        targetBaseBox.setPromptText("Select target base");

        Button addButton = new Button("Add");
        Button subtractButton = new Button("Subtract");
        Button convertButton = new Button("Convert Only");
        Label resultLabel = new Label();

        addButton.setOnAction(e -> performOperation(baseInput, numberInput1, numberInput2, targetBaseBox, resultLabel, "Add"));
        subtractButton.setOnAction(e -> performOperation(baseInput, numberInput1, numberInput2, targetBaseBox, resultLabel, "Subtract"));
        convertButton.setOnAction(e -> convertNumber(baseInput, numberInput1, targetBaseBox, resultLabel));

        customLayout.getChildren().addAll(baseLabel, baseInput, numberLabel1, numberInput1, numberLabel2, numberInput2, targetBaseBox, addButton, subtractButton, convertButton, resultLabel);

        Scene customScene = new Scene(customLayout, 400, 400);
        primaryStage.setScene(customScene);
    }

    private void openDefaultBaseWindow(Stage primaryStage) {
        VBox defaultLayout = new VBox(15);
        defaultLayout.setPadding(new Insets(20));
        defaultLayout.setAlignment(Pos.CENTER);

        ComboBox<String> fromBox = new ComboBox<>();
        fromBox.getItems().addAll("Binary", "Decimal", "Octal", "Hexadecimal");
        fromBox.setPromptText("From");

        ComboBox<String> toBox = new ComboBox<>();
        toBox.getItems().addAll("Binary", "Decimal", "Octal", "Hexadecimal");
        toBox.setPromptText("To");

        TextField numberInput1 = new TextField();
        numberInput1.setPromptText("Enter first number");

        TextField numberInput2 = new TextField();
        numberInput2.setPromptText("Enter second number (for addition/subtraction)");

        Button addButton = new Button("Add");
        Button subtractButton = new Button("Subtract");
        Button convertButton = new Button("Convert Only");
        Label resultLabel = new Label();

        addButton.setOnAction(e -> performOperation(fromBox, toBox, numberInput1, numberInput2, resultLabel, "Add"));
        subtractButton.setOnAction(e -> performOperation(fromBox, toBox, numberInput1, numberInput2, resultLabel, "Subtract"));
        convertButton.setOnAction(e -> convertNumber(fromBox, toBox, numberInput1, resultLabel));

        defaultLayout.getChildren().addAll(fromBox, toBox, numberInput1, numberInput2, addButton, subtractButton, convertButton, resultLabel);

        Scene defaultScene = new Scene(defaultLayout, 400, 400);
        primaryStage.setScene(defaultScene);
    }

    private void performOperation(TextField baseInput, TextField numberInput1, TextField numberInput2, ComboBox<String> targetBaseBox, Label resultLabel, String operation) {
        try {
            int base = Integer.parseInt(baseInput.getText());
            int num1 = Integer.parseInt(numberInput1.getText(), base);
            int num2 = Integer.parseInt(numberInput2.getText(), base);

            int result = operation.equals("Add") ? num1 + num2 : num1 - num2;

            String convertedResult = Integer.toString(result, getTargetBase(targetBaseBox.getValue())).toUpperCase();
            resultLabel.setText("Result (" + operation + "): " + convertedResult);
        } catch (Exception e) {
            resultLabel.setText("Invalid input or base.");
        }
    }

    private void performOperation(ComboBox<String> fromBox, ComboBox<String> toBox, TextField numberInput1, TextField numberInput2, Label resultLabel, String operation) {
        try {
            int fromBase = getTargetBase(fromBox.getValue());
            int num1 = Integer.parseInt(numberInput1.getText(), fromBase);
            int num2 = Integer.parseInt(numberInput2.getText(), fromBase);

            int result = operation.equals("Add") ? num1 + num2 : num1 - num2;

            String convertedResult = Integer.toString(result, getTargetBase(toBox.getValue())).toUpperCase();
            resultLabel.setText("Result (" + operation + "): " + convertedResult);
        } catch (Exception e) {
            resultLabel.setText("Invalid input or base.");
        }
    }

    private void convertNumber(TextField baseInput, TextField numberInput, ComboBox<String> targetBaseBox, Label resultLabel) {
        try {
            int base = Integer.parseInt(baseInput.getText());
            int number = Integer.parseInt(numberInput.getText(), base);
            String convertedResult = Integer.toString(number, getTargetBase(targetBaseBox.getValue())).toUpperCase();
            resultLabel.setText("Converted Value: " + convertedResult);
        } catch (Exception e) {
            resultLabel.setText("Invalid input or base.");
        }
    }

    private void convertNumber(ComboBox<String> fromBox, ComboBox<String> toBox, TextField numberInput, Label resultLabel) {
        try {
            int fromBase = getTargetBase(fromBox.getValue());
            int number = Integer.parseInt(numberInput.getText(), fromBase);
            String convertedResult = Integer.toString(number, getTargetBase(toBox.getValue())).toUpperCase();
            resultLabel.setText("Converted Value: " + convertedResult);
        } catch (Exception e) {
            resultLabel.setText("Invalid input or base.");
        }
    }

    private int getTargetBase(String baseName) {
        return switch (baseName) {
            case "Binary" -> 2;
            case "Octal" -> 8;
            case "Decimal" -> 10;
            case "Hexadecimal" -> 16;
            default -> throw new IllegalArgumentException("Invalid base");
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}
