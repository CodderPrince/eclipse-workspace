package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class BooleanSimplificationApp extends Application {

    private Label solutionLabel;
    private VBox stepsBox;
    private VBox referenceBox;

    @Override
    public void start(Stage primaryStage) {
        // Main Title Label
        Label titleLabel = new Label("Boolean Expression Simplifier");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.ORANGE);

        // Input Field for Boolean Expression
        Label inputLabel = new Label("Enter Boolean Expression:");
        inputLabel.setFont(Font.font("Arial", 18));
        TextField inputField = new TextField();
        inputField.setPromptText("e.g., AB + A'B + AB'");

        // Solution Section
        solutionLabel = new Label("Solution:");
        solutionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        solutionLabel.setTextFill(Color.ORANGE);

        stepsBox = new VBox();
        stepsBox.setSpacing(5);

        // Theorem References Section
        referenceBox = new VBox();
        referenceBox.setSpacing(5);
        
        Label referenceTitle = new Label("Theorems Used:");
        referenceTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        referenceTitle.setTextFill(Color.ORANGE);
        referenceBox.getChildren().add(referenceTitle);

        // Add Button to trigger simplification
        Button simplifyButton = new Button("Simplify");
        simplifyButton.setFont(Font.font("Arial", 16));
        simplifyButton.setOnAction(e -> {
            String expression = inputField.getText();
            simplifyExpression(expression);
        });

        // Layout for input and solution display
        HBox inputBox = new HBox(10, inputLabel, inputField, simplifyButton);
        inputBox.setSpacing(10);

        // Layout to align steps and references side by side
        HBox contentBox = new HBox();
        contentBox.setSpacing(40);
        contentBox.getChildren().addAll(stepsBox, referenceBox);

        // Main layout
        VBox root = new VBox(15);
        root.getChildren().addAll(titleLabel, inputBox, solutionLabel, contentBox);
        root.setStyle("-fx-padding: 20;");

        // Scene
        Scene scene = new Scene(root, 700, 500);
        primaryStage.setTitle("Boolean Expression Simplifier");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to simplify the expression and display solution
    private void simplifyExpression(String expression) {
        // Clear previous steps and references
        stepsBox.getChildren().clear();
        referenceBox.getChildren().clear();

        // Example: Simplification logic (basic rules, hard-coded for demonstration)
        expression = expression.replace(" ", ""); // Remove spaces for easier parsing

        // Display initial expression
        Label initialLabel = new Label("F = " + expression);
        initialLabel.setFont(Font.font("Arial", 18));
        stepsBox.getChildren().add(initialLabel);

        // Apply simplification rules
        Map<String, String> simplificationSteps = applySimplificationRules(expression);

        // Display each simplification step
        for (Map.Entry<String, String> entry : simplificationSteps.entrySet()) {
            Label stepLabel = new Label(entry.getKey() + " = " + entry.getValue());
            stepLabel.setFont(Font.font("Arial", 18));
            stepsBox.getChildren().add(stepLabel);
        }

        // Display Theorem References
        Label theorem1 = new Label("A + A' = 1");
        Label theorem2 = new Label("A . 1 = A");
        Label theorem3 = new Label("A + BC = (A + B)(A + C)");

        Label[] theorems = {theorem1, theorem2, theorem3};
        for (Label theorem : theorems) {
            theorem.setFont(Font.font("Arial", 16));
            theorem.setTextFill(Color.GOLDENROD);
            referenceBox.getChildren().add(theorem);
        }
    }

    private Map<String, String> applySimplificationRules(String expression) {
        Map<String, String> steps = new HashMap<>();
        
        // Example rule: Replace A + A' with 1
        if (expression.contains("A + A'")) {
            expression = expression.replace("A + A'", "1");
            steps.put("Apply A + A' = 1", expression);
        }

        // Example rule: Replace A . 1 with A
        if (expression.contains("A . 1")) {
            expression = expression.replace("A . 1", "A");
            steps.put("Apply A . 1 = A", expression);
        }

        // Example rule: Apply distributive law
        if (expression.contains("A + BC")) {
            expression = expression.replace("A + BC", "(A + B)(A + C)");
            steps.put("Apply A + BC = (A + B)(A + C)", expression);
        }

        // Additional simplification rules would be added here...

        return steps;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
