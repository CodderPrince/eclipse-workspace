/*
 * Title:         Number Conversion System
 * Developed by:  Md. An Nahian Prince
 * ID:            12105007
 * Availability:  Converts and performs arithmetic on custom/predefined bases 
 *                (Binary, Decimal, Octal, Hexadecimal).
 * Key Features:
 * - Custom base number operations
 * - Base conversions (Binary, Decimal, Octal, Hexadecimal, Custom)
 * - Addition and subtraction in any base
 * - Fractional number support
 * 
 * Limitation: Only converted upto 36 base only
 */

package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class NumberConversionSystem extends Application {

    @Override
    /******************************
     * 
     * Primary or First Window
     * 
     *****************************/

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Number Conversion System");

        VBox mainLayout = new VBox(50);

        mainLayout.setPadding(new Insets(20, 20, 20, 20));
        mainLayout.setAlignment(Pos.CENTER);

        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #ffd4c2, #ffe5d1\r\n"
                + ");");

        Label titleLabel = new Label("Number Conversion System");
        titleLabel.setFont(Font.font("Arial", 28));

        

        Button universalBaseButton = new Button("Universal Base Converter");
        setButtonStyle(universalBaseButton, "#0D47A1", 30);
        universalBaseButton.setOnAction(e -> openUniversalBaseConverter(primaryStage));

        mainLayout.getChildren().addAll(titleLabel, universalBaseButton);

        Scene mainScene = new Scene(mainLayout, 450, 500);
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    

    /****************************************
     * 
     * Else press "Universal Base Converter"
     * Open Second Display
     * 
     ****************************************/
    private void openUniversalBaseConverter(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #b7eaff, #94dfff);");

        Label fromBaseLabel = new Label("From Base:");
        fromBaseLabel.setStyle("-fx-font-size: 17px; " +
                "-fx-font-family: 'Arial Rounded MT Bold'; " +
                "-fx-text-fill: blue;");

        TextField fromBaseInput = new TextField();
        fromBaseInput.setPromptText("Enter original base");
        fromBaseInput.setStyle("-fx-font-size: 17px; " +
                "-fx-font-family: 'Arial Rounded MT Bold'; " +
                "-fx-text-fill: blue;");
        Label numberLabel = new Label("Number to Convert:");
        numberLabel.setStyle("-fx-font-size: 17px; " +
                "-fx-font-family: 'Arial Rounded MT Bold'; " +
                "-fx-text-fill: #006400;");

        TextField numberInput = new TextField();
        numberInput.setPromptText("Enter number");
        numberInput.setStyle("-fx-font-size: 17px; " +
                "-fx-font-family: 'Arial Rounded MT Bold'; " +
                "-fx-text-fill: #006400;");

        Label toBaseLabel = new Label("To Base:");
        toBaseLabel.setStyle("-fx-font-size: 17px; " +
                "-fx-font-family: 'Arial Rounded MT Bold'; " +
                "-fx-text-fill: #4B0082;");

        TextField toBaseInput = new TextField();
        toBaseInput.setPromptText("Enter target base");
        toBaseInput.setStyle("-fx-font-size: 17px; " +
                "-fx-font-family: 'Arial Rounded MT Bold'; " +
                "-fx-text-fill: #4B0082;");

        Button convertButton = new Button("Convert");
        setButtonStyle(convertButton, "#4CAF50", 20);
        Label resultLabel = new Label();

        convertButton.setOnAction(e -> {
            try {
                int fromBase = Integer.parseInt(fromBaseInput.getText());
                int toBase = Integer.parseInt(toBaseInput.getText());
                String number = numberInput.getText();
                String result = convertNumberBetweenBases(number, fromBase, toBase);
                resultLabel.setText("Result: " + result);

                resultLabel.setStyle(
                        "-fx-font-size: 22px; -fx-text-fill: blue; -fx-font-family: 'Arial Rounded MT Bold';"); // Set
                                                                                                                // to
                                                                                                                // blue
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid base or number format.");
                resultLabel
                        .setStyle("-fx-font-size: 17px; -fx-text-fill: red; -fx-font-family: 'Arial Rounded MT Bold';");
            }
        });

        Button backButton = new Button("Back");
        setButtonStyle(backButton, "#f44336", 20);
        backButton.setOnAction(e -> start(primaryStage));

        layout.getChildren().addAll(fromBaseLabel, fromBaseInput, numberLabel, numberInput, toBaseLabel, toBaseInput,
                convertButton, resultLabel, backButton);

        Scene converterScene = new Scene(layout, 400, 430);
        primaryStage.setScene(converterScene);
    }

    /**********************************************
     * 
     * Second Window for Universal Base Converter
     * 
     **********************************************/
    private String convertNumberBetweenBases(String number, int fromBase, int toBase) {
        StringBuilder result = new StringBuilder();
        String[] parts = number.split("\\.");

        int integerPart = Integer.parseInt(parts[0], fromBase);
        result.append("1. Convert " + number + " from base " + fromBase + " to decimal.\n");
        result.append("   - Integer part: " + integerPart + "\n");

        double fractionalPart = 0;

        if (parts.length > 1) {
            StringBuilder fractionalDetails = new StringBuilder("   - Fractional part calculated as:\n");

            for (int i = 0; i < parts[1].length(); i++) {
                int digitValue = Character.digit(parts[1].charAt(i), fromBase);
                fractionalPart += digitValue / Math.pow(fromBase, i + 1);
                fractionalDetails.append("     + " + digitValue + " * " + fromBase + "^(-" + (i + 1) + ")\n");
            }

            result.append(fractionalDetails);
        }

        result.append("   - Result in Fractional: "  + "."
                + String.format("%.15f", fractionalPart).substring(2) + "\n");

        result.append("2. Result in decimal: " + integerPart + "." + String.format("%.15f", fractionalPart).substring(2)
                + "\n");

        double decimalValue = integerPart + fractionalPart;
        result.append("3. Convert decimal " + decimalValue + " to base " + toBase + ".\n");
        
        String integerResult = Integer.toString(integerPart, toBase).toUpperCase();
        result.append("   - Result: " + integerResult);

        StringBuilder fractionalStr = new StringBuilder(".");
        while (fractionalPart != 0 && fractionalStr.length() < 20) {
            fractionalPart *= toBase;
            int fractionalIntPart = (int) fractionalPart;
            fractionalStr.append(Integer.toString(fractionalIntPart, toBase).toUpperCase());
            fractionalPart -= fractionalIntPart;
        }

        result.append(fractionalStr);
        result.append("\n");

        return result.toString();
    }

    

    /*****************************************************
     * 
     * Styling method to easily set button color and font
     * Button color font and fill change
     * 
     *****************************************************/
    private void setButtonStyle(Button button, String color, int fontSize) {
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        button.setFont(Font.font("Arial", fontSize));
    }

    /*******************
     * 
     * Main Function
     * 
     ******************/
    public static void main(String[] args) {
        launch(args);
    }
}