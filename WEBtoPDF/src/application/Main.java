package application;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		// Create UI elements
		TextField urlField = new TextField();
		urlField.setPromptText("Enter webpage URL");

		ComboBox<String> formatComboBox = new ComboBox<>();
		formatComboBox.getItems().addAll("PDF", "WebP", "PNG");
		formatComboBox.setValue("PDF");

		Button convertButton = new Button("Convert");

		Label statusLabel = new Label();

		// Handle the Convert button click event
		convertButton.setOnAction(e -> {
			String url = urlField.getText();
			String format = formatComboBox.getValue();

			if (url.isEmpty()) {
				statusLabel.setText("Please enter a valid URL.");
			} else {
				statusLabel.setText("Converting...");

				// Here you will call the function that converts the webpage
				File outputFile = convertWebPageToFile(url, format);

				// After conversion, show file chooser to save the file
				if (outputFile != null) {
					saveFileDialog(primaryStage, outputFile);
				}
			}
		});

		// Set up the layout
		VBox vbox = new VBox(10, urlField, formatComboBox, convertButton, statusLabel);
		vbox.setStyle("-fx-padding: 20;");

		// Set the scene
		Scene scene = new Scene(vbox, 400, 200);
		primaryStage.setTitle("Webpage to File Converter");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Simulated conversion process (replace with actual logic)
	private File convertWebPageToFile(String url, String format) {
		// Simulate the process, but you need to call actual code to convert the webpage
		try {
			System.out.println("Converting " + url + " to " + format + " format...");
			Thread.sleep(2000); // Simulate delay for conversion

			// Create a dummy file for demonstration (replace this with actual file
			// creation)
			File outputFile = new File("E:\\abc." + format.toLowerCase()); // Replace with dynamic path and format
			System.out.println("Conversion to " + format + " successful!");

			// Call actual conversion logic (e.g., via Puppeteer or another tool)
			// For example, execute Node.js Puppeteer code here

			// Simulate saving the actual file
			saveConvertedFile(outputFile); // Actual file save

			return outputFile;

		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Simulate saving the converted file (implement actual logic here)
	private void saveConvertedFile(File outputFile) {
		try {
			// Simulate file save (you can write content here, if needed)
			System.out.println("File saved to: " + outputFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Open file chooser to save the converted file
	private void saveFileDialog(Stage primaryStage, File outputFile) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
				outputFile.getName().split("\\.")[1].toUpperCase(), "*." + outputFile.getName().split("\\.")[1]));

		// Open the save file dialog
		fileChooser.setInitialFileName(outputFile.getName());
		File savedFile = fileChooser.showSaveDialog(primaryStage);

		if (savedFile != null) {
			System.out.println("File saved to: " + savedFile.getAbsolutePath());
			// Save the file (simulated here, in reality, you'd save the converted content
			// to the file)
			// For now, just print that the file is saved successfully
			System.out.println("File saved successfully!");
		} else {
			System.out.println("Save operation was cancelled.");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
