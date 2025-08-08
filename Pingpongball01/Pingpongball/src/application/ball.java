package application;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ball extends Application{
	
	// Variables
	private static final double width = 800;
	private static final double height = 600;
	private static final double PLAYER_HEIGHT = 100;
	private static final double PLAYER_WIDTH = 15;
	private static final double BALL_R = 15;
	private double playerOneXPos = 0;
	private double playerOneYPos = height / 2;
	private double playerTwoXPos = width - PLAYER_WIDTH;
	private double playerTwoYPos = height / 2;
	private double ballXPos = width / 2;
	private double ballYPos = height / 2;
	private boolean gameStarted;
	private int ballXSpeed = 1;
	private int ballYSpeed = 1;
	private int scoresP1 = 0;
	private int scoresP2 = 0;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Primary stage settings
		primaryStage.setTitle("Ping-Pong Game");
		primaryStage.setResizable(false);
		
		// Layout for scene 1
		BorderPane borderPane = new BorderPane();
		Canvas canvas = new Canvas(width, height);	
		borderPane.setCenter(canvas);
		HBox hbButtons = new HBox();
	    hbButtons.setSpacing(33.0);
	    borderPane.setBottom(hbButtons);
	    HBox hbTitle = new HBox();
	    borderPane.setTop(hbTitle);
		Scene scene1 = new Scene((new StackPane(borderPane)));
		
		// Set title
		Label titleText = new Label("PING-PONG GAME");		
		titleText.setId("titleText");
		
		hbTitle.getChildren().addAll(titleText);
		hbTitle.setAlignment(Pos.CENTER);
		
		// Set start button
		Button startButton = new Button("START GAME");
		startButton.setId("startButton");
		
		// Set reset button
		Button resetButton = new Button("RESET GAME");
		resetButton.setId("resetButton");

		hbButtons.getChildren().addAll(startButton, resetButton);
		hbButtons.setAlignment(Pos.CENTER);
		 		
		// Set graphics
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();	
		
		// JavaFX Timeline - free form animation defined by KeyFrames and their duration 
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> runGame(graphicsContext, canvas)));
		// Number of cycles in animation INDEFINITE = repeat indefinitely, it can be defined but must be > 0
		timeline.setCycleCount(Timeline.INDEFINITE);
		
		// Button "start" control 
		startButton.setOnAction(e ->  {
			gameStarted = true;
			if(scoresP1 == 5 || scoresP2 == 5) {
				scoresP1 = 0;
				scoresP2 = 0;
			}
		});
		
		// Button "reset" control 
		resetButton.setOnAction(e ->  {
			gameStarted = false;
			scoresP1 = 0;
			scoresP2 = 0;
		});
		
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());		
		primaryStage.setScene(scene1);
		primaryStage.show();
		timeline.play();
	}

	private void runGame(GraphicsContext graphicsContext, Canvas canvas) {
		// Set background color
		graphicsContext.setFill(Color.FORESTGREEN);
		graphicsContext.fillRect(0, 0, width, height);
				
		// Set text
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.setFont(Font.font("Verdana", 25));
		
		// Draw player 1 & 2
		graphicsContext.fillRect(playerTwoXPos, playerTwoYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
		graphicsContext.fillRect(playerOneXPos, playerOneYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
		
		// Draw scores
		graphicsContext.fillText(scoresP1 + "\t\t\t\t\t\t\t" + scoresP2, width / 2, 50);
		
		if(gameStarted) {
			// Draw line in the middle
			graphicsContext.setStroke(Color.WHITE);
			graphicsContext.setLineWidth(1);
			graphicsContext.strokeLine(width / 2, height, width / 2, 0);
			
			// Mouse control on move
			canvas.setOnMouseMoved(e -> playerOneYPos = e.getY());
			
			// Set computer opponent following the ball
			if(ballXPos < width - width / 4) {
				playerTwoYPos = ballYPos - PLAYER_HEIGHT / 2;
			}  
			else {
				playerTwoYPos = ballYPos > playerTwoYPos + PLAYER_HEIGHT / 2 ?playerTwoYPos += 1: playerTwoYPos - 1;
			}
			
			// Draw the ball
			graphicsContext.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);
			
			// Set ball movement
			ballXPos+=ballXSpeed;
			ballYPos+=ballYSpeed;
			
			// Set reflection effect to null
			graphicsContext.setEffect(null);
		}
		else if (scoresP1 == 5 || scoresP2 == 5) {
			gameStarted = false;
			// Set the start text
			graphicsContext.setFill(Color.RED);
			graphicsContext.setFont(new Font("Verdana", 30));
			graphicsContext.setTextAlign(TextAlignment.CENTER);
			graphicsContext.fillText("Game over", width / 2, height / 2);	
			
			// Set reflection effect
			Reflection r = new Reflection();
			r.setFraction(0.7f);			 
			graphicsContext.setEffect(r);
			
			// Reset the ball start position 
			ballXPos = width / 2;
			ballYPos = height / 2;
			
			// Reset the ball speed and the direction
			ballXSpeed = new Random().nextInt(2) == 0 ? 1: -1;
			ballYSpeed = new Random().nextInt(2) == 0 ? 1: -1;
			
			// Mouse control on move stopped
			canvas.setOnMouseMoved(null);
		}
		else {
			// Set the start text
			graphicsContext.setFill(Color.YELLOW);
			graphicsContext.setFont(new Font("Verdana", 30));
			graphicsContext.setTextAlign(TextAlignment.CENTER);
			graphicsContext.fillText("Click the button to start game", width / 2, height / 2);	
			
			// Set reflection effect
			Reflection r = new Reflection();
			r.setFraction(0.7f);			 
			graphicsContext.setEffect(r);
			
			// Reset the ball start position 
			ballXPos = width / 2;
			ballYPos = height / 2;
			
			// Reset the ball speed and the direction
			ballXSpeed = new Random().nextInt(2) == 0 ? 1: -1;
			ballYSpeed = new Random().nextInt(2) == 0 ? 1: -1;
			
			// Mouse control on move stopped
			canvas.setOnMouseMoved(null);
		}
		
		// Set that the ball have to stay in canvas / on the "screen"
		if(ballYPos > height || ballYPos < 0) ballYSpeed *=-1;
					
		// Increase speed of the ball after player hits it
		if( ((ballXPos + BALL_R > playerTwoXPos) && ballYPos >= playerTwoYPos && ballYPos <= playerTwoYPos + PLAYER_HEIGHT) || 
			((ballXPos < playerOneXPos + PLAYER_WIDTH) && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + PLAYER_HEIGHT)) {
				ballXSpeed += 1 * Math.signum(ballXSpeed);			
				ballYSpeed += 1 * Math.signum(ballYSpeed);
				ballXSpeed *= -1;
				ballYSpeed *= -1;				
		}
		
		// If player misses the ball, computer gets a point + reset game
		if(ballXPos < playerOneXPos - PLAYER_WIDTH) {
			scoresP2++;
			gameStarted = false;
		}
					
		// If computer misses the ball, player gets a point + reset game
		if(ballXPos > playerTwoXPos + PLAYER_WIDTH) {  
			scoresP1++;
			gameStarted = false;
		}
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}