package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class NumberConversionSystem extends Application {

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

        Label numberLabel = new Label("Enter Your Base Number:");
        TextField numberInput = new TextField();
        numberInput.setPromptText("Enter your base number");

        ComboBox<String> targetBaseBox = new ComboBox<>();
        targetBaseBox.getItems().addAll("Decimal", "Binary", "Octal", "Hexadecimal");
        targetBaseBox.setPromptText("Select target base");

        Button convertButton = new Button("Convert");
        Label resultLabel = new Label();

        convertButton.setOnAction(e -> {
            String baseText = baseInput.getText();
            String number = numberInput.getText();
            String targetBase = targetBaseBox.getValue();

            // Check if inputs are valid
            if (baseText != null && !baseText.isEmpty() && number != null && !number.isEmpty() && targetBase != null) {
                try {
                    int fromRadix = Integer.parseInt(baseText); // Parse the base entered by the user
                    if (fromRadix < 2 || fromRadix > 36) {
                        resultLabel.setText("Please enter a base between 2 and 36.");
                        return;
                    }
                    resultLabel.setText("Converted value:\n" + convertBase(number, fromRadix, targetBase));
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Invalid base or number format.");
                }
            } else {
                resultLabel.setText("Please enter all fields.");
            }
        });

        customLayout.getChildren().addAll(baseLabel, baseInput, numberLabel, numberInput, targetBaseBox, convertButton, resultLabel);

        Scene customScene = new Scene(customLayout, 400, 350);
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

        TextField numberInput = new TextField();
        numberInput.setPromptText("Enter number to convert");

        Button convertButton = new Button("Convert");
        Label resultLabel = new Label();

        convertButton.setOnAction(e -> {
            String number = numberInput.getText();
            String fromBase = fromBox.getValue();
            String toBase = toBox.getValue();
            if (number != null && !number.isEmpty() && fromBase != null && toBase != null) {
                resultLabel.setText("Converted value:\n" + convertBase(number, fromBase, toBase));
            } else {
                resultLabel.setText("Please enter a number and select both bases.");
            }
        });

        defaultLayout.getChildren().addAll(fromBox, toBox, numberInput, convertButton, resultLabel);

        Scene defaultScene = new Scene(defaultLayout, 400, 300);
        primaryStage.setScene(defaultScene);
    }

    private String convertBase(String number, int fromRadix, String toBase) {
        int toRadix;

        // Determine the radix (base) for the "to" base
        switch (toBase) {
            case "Binary":
                toRadix = 2;
                break;
            case "Octal":
                toRadix = 8;
                break;
            case "Decimal":
                toRadix = 10;
                break;
            case "Hexadecimal":
                toRadix = 16;
                break;
            default:
                return "Unsupported target base: " + toBase;
        }

        try {
            // Separate integer and fractional parts
            String[] parts = number.split("\\.");
            String integerPart = parts[0];
            String fractionalPart = (parts.length > 1) ? parts[1] : null;

            // Convert the integer part
            int integerDecimalValue = Integer.parseInt(integerPart, fromRadix);
            String integerResult = Integer.toString(integerDecimalValue, toRadix).toUpperCase();

            // Convert the fractional part if it exists
            StringBuilder fractionalResult = new StringBuilder();
            if (fractionalPart != null) {
                double fraction = 0;
                for (int i = 0; i < fractionalPart.length(); i++) {
                    int digitValue = Character.digit(fractionalPart.charAt(i), fromRadix);
                    if (digitValue == -1) {
                        return "Invalid input for base " + fromRadix;
                    }
                    fraction += digitValue / Math.pow(fromRadix, i + 1);
                }

                // Convert the fractional part to the target base
                fractionalResult.append(".");
                for (int i = 0; i < 10; i++) { // Limit to 10 digits for precision
                    fraction *= toRadix;
                    int digitValue = (int) fraction;
                    fractionalResult.append(Character.forDigit(digitValue, toRadix));
                    fraction -= digitValue;
                    if (fraction == 0) break;
                }
            }

            return "Converted Value (" + toBase + "): " + integerResult + fractionalResult.toString();
        } catch (NumberFormatException e) {
            return "Invalid input for base " + fromRadix;
        }
    }

    private String convertBase(String number, String fromBase, String toBase) {
        int fromRadix;
        int toRadix;

        // Determine the radix (base) for the "from" and "to" bases
        switch (fromBase) {
            case "Binary":
                fromRadix = 2;
                break;
            case "Octal":
                fromRadix = 8;
                break;
            case "Decimal":
                fromRadix = 10;
                break;
            case "Hexadecimal":
                fromRadix = 16;
                break;
            default:
                throw new IllegalArgumentException("Unsupported base: " + fromBase);
        }

        switch (toBase) {
            case "Binary":
                toRadix = 2;
                break;
            case "Octal":
                toRadix = 8;
                break;
            case "Decimal":
                toRadix = 10;
                break;
            case "Hexadecimal":
                toRadix = 16;
                break;
            default:
                throw new IllegalArgumentException("Unsupported base: " + toBase);
        }

        try {
            // Separate integer and fractional parts
            String[] parts = number.split("\\.");
            String integerPart = parts[0];
            String fractionalPart = (parts.length > 1) ? parts[1] : null;

            // Convert integer part
            int integerDecimalValue = Integer.parseInt(integerPart, fromRadix);
            String integerResult = Integer.toString(integerDecimalValue, toRadix).toUpperCase();

            // Convert fractional part if it exists
            StringBuilder fractionalResult = new StringBuilder();
            if (fractionalPart != null) {
                double fraction = 0;
                for (int i = 0; i < fractionalPart.length(); i++) {
                    int digitValue = Character.digit(fractionalPart.charAt(i), fromRadix);
                    if (digitValue == -1) {
                        return "Invalid input for base " + fromRadix;
                    }
                    fraction += digitValue / Math.pow(fromRadix, i + 1);
                }

                // Convert the fractional part to the target base
                fractionalResult.append(".");
                for (int i = 0; i < 10; i++) { // Limit to 10 digits for precision
                    fraction *= toRadix;
                    int digitValue = (int) fraction;
                    fractionalResult.append(Character.forDigit(digitValue, toRadix));
                    fraction -= digitValue;
                    if (fraction == 0) break;
                }
            }

            return "Converted Value (" + toBase + "): " + integerResult + fractionalResult.toString();
        } catch (NumberFormatException e) {
            return "Invalid input for base " + fromBase;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
