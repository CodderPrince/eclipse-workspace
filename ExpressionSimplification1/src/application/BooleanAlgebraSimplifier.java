package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class BooleanAlgebraSimplifier extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Input field for boolean expressions
        TextField inputField = new TextField();
        inputField.setPromptText("Enter Boolean Expression");

        // Button to trigger simplification
        Button simplifyButton = new Button("Simplify");

        // Text area for displaying the simplified result
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        // Text area for displaying the reference rules used
        TextArea referenceArea = new TextArea();
        referenceArea.setEditable(false);
        referenceArea.setText("A + Ā = 1\nA . 1 = A\nA . Ā = 0\nA + 0 = A");

        // Layout setup
        VBox layout = new VBox(10, inputField, simplifyButton, new Label("Simplified Expression:"), resultArea, new Label("References:"), referenceArea);
        layout.setPadding(new Insets(15));
        
        // Set the action on button click
        simplifyButton.setOnAction(e -> {
            String expression = inputField.getText();
            String simplifiedExpression = BooleanSimplifier.simplifyExpression(expression); // Use the BooleanSimplifier method
            resultArea.setText(simplifiedExpression);
        });

        // Setting the scene and stage
        Scene scene = new Scene(layout, 400, 500);
        primaryStage.setTitle("Boolean Algebra Simplifier");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// BooleanSimplifier class for simplifying Boolean expressions
class BooleanSimplifier {

    // Simplifies a Boolean expression
    public static String simplifyExpression(String expression) {
        int numVariables = countVariables(expression);
        Set<String> variables = extractVariables(expression);
        List<String> varList = new ArrayList<>(variables);
        StringBuilder simplifiedExpression = new StringBuilder();

        // Generate truth table and find minterms
        int rows = (int) Math.pow(2, numVariables);
        for (int i = 0; i < rows; i++) {
            Map<String, Boolean> values = new HashMap<>();
            for (int j = 0; j < numVariables; j++) {
                values.put(varList.get(j), (i & (1 << j)) != 0);
            }
            if (evaluateExpression(expression, values)) {
                simplifiedExpression.append(mintermToString(values, varList));
                simplifiedExpression.append(" + ");
            }
        }
        
        if (simplifiedExpression.length() > 0) {
            simplifiedExpression.setLength(simplifiedExpression.length() - 3); // Remove last " + "
        } else {
            return "0"; // If no minterms, expression is always false
        }

        return simplifiedExpression.toString();
    }

    // Evaluates the expression given a set of variable assignments
    private static boolean evaluateExpression(String expression, Map<String, Boolean> values) {
        // This would be where you integrate a Boolean evaluation logic
        // Placeholder for demonstration
        return new Random().nextBoolean(); // Replace with actual evaluation
    }

    // Converts a minterm represented by a map of variable values to a string
    private static String mintermToString(Map<String, Boolean> minterm, List<String> varList) {
        StringBuilder result = new StringBuilder();
        for (String var : varList) {
            if (minterm.get(var)) {
                result.append(var);
            } else {
                result.append(var).append("'");
            }
        }
        return result.toString();
    }

    // Extracts variable names from the expression
    private static Set<String> extractVariables(String expression) {
        Set<String> variables = new HashSet<>();
        for (char c : expression.toCharArray()) {
            if (Character.isLetter(c)) {
                variables.add(String.valueOf(c));
            }
        }
        return variables;
    }

    // Counts unique variables in the expression
    private static int countVariables(String expression) {
        return extractVariables(expression).size();
    }
}
