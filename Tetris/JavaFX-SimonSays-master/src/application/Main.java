package application;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	
	private static final String PATH =  "/sounds/";

	private ArrayList<Integer> player, computer;
	private MediaPlayer btn1s, btn2s, btn3s, btn4s;

	private Scene scene;
	private Button btn1, btn2, btn3, btn4, start, replay;

	private int id, currentPlayer, numberClick, current;

	private BorderPane root;
	private Pane pane;
	private HBox hbox;

	private SequentialTransition sequence;
	private PauseTransition transition;

	/**
	 * Initializes and reset some variables to start and replay the Game
	 */
	public void reset() {
		id = 1;
		currentPlayer = 1;
		numberClick = 0;

		btn1.setDisable(false);
		btn2.setDisable(false);
		btn3.setDisable(false);
		btn4.setDisable(false);

		player = new ArrayList<Integer>();
		computer = new ArrayList<Integer>();
	}

	@Override
	public void start(Stage primaryStage) {

		// Initializes every sound
		btn4s = new MediaPlayer(new Media(getClass().getResource(PATH + "4.wav").toExternalForm()));
		btn3s = new MediaPlayer(new Media(getClass().getResource(PATH + "3.wav").toExternalForm()));
		btn2s = new MediaPlayer(new Media(getClass().getResource(PATH + "2.wav").toExternalForm()));
		btn1s = new MediaPlayer(new Media(getClass().getResource(PATH + "1.wav").toExternalForm()));

		// Start button
		start = new Button("Start");
		start.setOnAction(e -> {
			reset();
			computer();
			start.setDisable(true);
			replay.setDisable(false);
		});

		// Replay button
		replay = new Button("Replay");
		replay.setDisable(true);
		replay.setOnAction(e -> {
			reset();
			computer();
		});

		// Yellow button
		btn1 = new Button();
		btn1.setId("yellow");
		btn1.setLayoutX(30);
		btn1.setLayoutY(20);
		btn1.setPrefSize(160, 160);
		btn1.setOnAction(e -> {
			btn1s.seek(Duration.ZERO);
			btn1s.play();
			check(1);
		});

		// Blue button
		btn2 = new Button();
		btn2.setId("blue");
		btn2.setLayoutX(210);
		btn2.setLayoutY(20);
		btn2.setPrefSize(160, 160);
		btn2.setOnAction(e -> {
			btn2s.seek(Duration.ZERO);
			btn2s.play();
			check(2);
		});

		// Red button
		btn3 = new Button();
		btn3.setId("red");
		btn3.setLayoutX(30);
		btn3.setLayoutY(200);
		btn3.setPrefSize(160, 160);
		btn3.setOnAction(e -> {
			btn3s.seek(Duration.ZERO);
			btn3s.play();
			check(3);
		});

		// Green button
		btn4 = new Button();
		btn4.setId("green");
		btn4.setLayoutX(210);
		btn4.setLayoutY(200);
		btn4.setPrefSize(160, 160);
		btn4.setOnAction(e -> {
			btn4s.seek(Duration.ZERO);
			btn4s.play();
			check(4);
		});

		// Layout of the four buttons
		pane = new Pane();
		pane.getChildren().addAll(btn1, btn2, btn3, btn4);

		// Layout of the top buttons
		hbox = new HBox();
		hbox.setSpacing(10);
		hbox.getChildren().addAll(start, replay);
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(20, 0, 0, 0));

		// Layout of the Game
		root = new BorderPane();
		root.setId("back");
		root.setTop(hbox);
		root.setCenter(pane);

		// Sets the scene to the BorderPane layout and links the CSS file
		scene = new Scene(root, 390, 420);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		// Sets the stage, makes it not resizable, sets its title, and displays it
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Simon Says");
		primaryStage.show();
	}

	/**
	 * Checks whether the current button cliked by the player was in the right order
	 * than the one that the computer did
	 * 
	 * @param num
	 *            the number of the button
	 */
	public void check(int num) {
		if (currentPlayer == 0) {
			player.add(num);

			if (player.get(numberClick) != computer.get(numberClick)) {
				btn1.setDisable(true);
				btn2.setDisable(true);
				btn3.setDisable(true);
				btn4.setDisable(true);
			}

			numberClick++;

			if (player.size() == computer.size()) {
				if (player.get(numberClick - 1) == computer.get(numberClick - 1)) {
					computer();
				}
				player.clear();
				numberClick = 0;
			}
		}
	}

	/**
	 * The method that makes the computer's animations and decisions
	 */
	public void computer() {
		currentPlayer = 1;

		sequence = new SequentialTransition();

		for (int i = 1; i < 4; i++) {

			if (id == 1) {
				current = ThreadLocalRandom.current().nextInt(1, 4 + 1);

				if (computer.size() > 0) {
					while (computer.get(computer.size() - 1) == current) {
						current = ThreadLocalRandom.current().nextInt(1, 4 + 1);
					}
				}

				computer.add(current);
			}

			current = computer.get(i - 1);

			transition = new PauseTransition(Duration.millis(500));
			if (current == 1) {
				transition.setOnFinished(evt -> button1());
			} else if (current == 2) {
				transition.setOnFinished(evt -> button2());
			} else if (current == 3) {
				transition.setOnFinished(evt -> button3());
			} else {
				transition.setOnFinished(evt -> button4());
			}

			sequence.getChildren().add(transition);
		}

		if (id != 1) {
			current = ThreadLocalRandom.current().nextInt(1, 4 + 1);

			if (computer.size() > 0) {
				while (computer.get(computer.size() - 1) == current) {
					current = ThreadLocalRandom.current().nextInt(1, 4 + 1);
				}
			}

			computer.add(current);

			for (int i = 1; i < id; i++) {
				current = computer.get(2 + i);

				transition = new PauseTransition(Duration.millis(500));
				if (current == 1) {
					transition.setOnFinished(evt -> button1());
				} else if (current == 2) {
					transition.setOnFinished(evt -> button2());
				} else if (current == 3) {
					transition.setOnFinished(evt -> button3());
				} else {
					transition.setOnFinished(evt -> button4());
				}

				sequence.getChildren().add(transition);
			}
		}

		transition = new PauseTransition(Duration.millis(500));
		transition.setOnFinished(evt -> currentPlayer = 0);
		sequence.getChildren().add(transition);
		sequence.play();
		id++;
	}

	/**
	 * Plays an animation that presses the yellow button
	 */
	public void button1() {
		PauseTransition buttonPress = new PauseTransition(Duration.millis(200));
		buttonPress.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				btn1.disarm();
				btn1.fire();
			}
		});
		btn1.arm();
		buttonPress.play();
	}

	/**
	 * Plays an animation that presses the blue button
	 */
	public void button2() {
		PauseTransition buttonPress = new PauseTransition(Duration.millis(200));
		buttonPress.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				btn2.disarm();
				btn2.fire();
			}
		});
		btn2.arm();
		buttonPress.play();
	}

	/**
	 * Plays an animation that presses the red button
	 */
	public void button3() {
		PauseTransition buttonPress = new PauseTransition(Duration.millis(200));
		buttonPress.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				btn3.disarm();
				btn3.fire();
			}
		});
		btn3.arm();
		buttonPress.play();
	}

	/**
	 * Plays an animation that presses the green button
	 */
	public void button4() {
		PauseTransition buttonPress = new PauseTransition(Duration.millis(200));
		buttonPress.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				btn4.disarm();
				btn4.fire();
			}
		});
		btn4.arm();
		buttonPress.play();
	}

	/**
	 * Main method of the Simon Says game
	 */
	public static void main(String[] args) {
		launch(args);
	}
}