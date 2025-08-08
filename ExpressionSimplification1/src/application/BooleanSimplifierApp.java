package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BooleanSimplifierApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // UI Elements
        Label inputLabel = new Label("Enter Boolean Expression:");
        TextField inputField = new TextField();
        Button simplifyButton = new Button("Simplify");
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        simplifyButton.setOnAction(e -> {
            String expression = inputField.getText();
            String simplifiedExpression = simplifyExpression(expression);
            outputArea.setText(simplifiedExpression);
        });

        VBox root = new VBox(10, inputLabel, inputField, simplifyButton, outputArea);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Boolean Expression Simplifier");
        primaryStage.show();
    }

    private String simplifyExpression(String expression) {
        // Replace "A'" with "¬A" or keep it as "A'" if you want to display it in that form
        expression = expression.replaceAll("A'", "¬A");
        expression = expression.replaceAll("B'", "¬B");
        expression = expression.replaceAll("C'", "¬C");
        
        // Remove all whitespaces for easier processing
        expression = expression.replaceAll("\\s+", "");

        // Apply De Morgan's laws
        expression = expression.replaceAll("¬\\((A\\+B)\\)", "¬A*¬B"); // Ex: ¬(A + B) -> ¬A * ¬B
        expression = expression.replaceAll("¬\\((A\\*B)\\)", "¬A+¬B"); // Ex: ¬(A * B) -> ¬A + ¬B

        // Apply basic complement rules
        expression = expression.replaceAll("A\\+¬A", "1"); // A + ¬A = 1
        expression = expression.replaceAll("A\\*¬A", "0"); // A * ¬A = 0

        // Apply identity laws
        expression = expression.replaceAll("A\\+0", "A");        // A + 0 = A
        expression = expression.replaceAll("A\\*1", "A");        // A * 1 = A

        // Apply idempotent laws
        expression = expression.replaceAll("A\\+A", "A");        // A + A = A
        expression = expression.replaceAll("A\\*A", "A");        // A * A = A

        // Apply absorption laws
        expression = expression.replaceAll("A\\+A\\*B", "A");    // A + A * B = A
        expression = expression.replaceAll("A\\*(A\\+B)", "A");  // A * (A + B) = A

        return "Simplified Expression:\n" + expression;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
