/*------------------------------------------
 *  Group Members: Cristina E Villarroel
 *                  Jose G Perez
 * 
 * Student IDs:    A32825495
 * 				   G95764997
 * 
 * COP 2805 - Advanced Java Programming
 * 
 * Spring 2015 - T Th 6:00PM - 9:20PM
 * 
 * Project # 4
 * 
 * Plagiarism Statement: I certify that this 
 * assignment is my own work and that I have
 * not copied in part or whole or otherwise 
 * plagiarized the work of other students
 * and/or persons....
 *----------------------------------------*/

import java.util.LinkedList;
import java.util.Queue;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class SpaceRace extends Application {
	// Declaring and initilise some variables here some we can change them in
	// the methods.
	// Variables for the Timer
	static final Integer STARTTIME = 3;
	static Label timerLabel = new Label();
	static IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);
	static Timeline timeline;

	// Threads
	Thread threadRace;
	Thread threadResult;
	
	// Rest of Variables
	boolean racing = false;
	Queue<String> finishedOrder = new LinkedList<String>();
	double bet;
	String userChoice;

	@Override
	public void start(Stage primaryStage) {
		Pane pane      = new Pane();
		Group paneRace = new Group();
                Button button1 = new Button("OK");
                
                button1.setAlignment(Pos.CENTER);
                
		// This is just calling the methods to draw everything
		loadBackground(pane);
		drawMoon(pane, 230, 100, 25.0);
		drawSun(pane, 450, 450, 35);
		drawSaturn(pane, 600, 300, 5, 315);
		drawTrack(pane);

                Label instructions = new Label("Welcome!! This is a racing game\n"
                                             + "to help the stitches get home.\n"
                                             + "You need to select one spaceship\n"
                                             + "color (who you think is gonna win)\n"
                                             + "and then how much you want to bet.\n"
                                             + "May the odds be in your favor!!\n");
                
                instructions.setFont(Font.font("Impact", FontWeight.BOLD, 20));
                instructions.setAlignment(Pos.CENTER);
                
		// Filling the a rectangle that is going to be the back of our menu with
		// Gradient
		// Choose two Colors
		Stop[] paintGradientMenu = new Stop[] { new Stop(0, Color.GRAY),
				new Stop(1, Color.SILVER) };

		// Fills with a linear color gradient pattern
		LinearGradient gray = new LinearGradient(0, 0, 0, 1, true,
				CycleMethod.NO_CYCLE, paintGradientMenu);

		// Create our Menu back with rounded corners
                Screen screen = Screen.getPrimary();
                
                Rectangle2D bounds = screen.getVisualBounds();
                Rectangle menu = new Rectangle(0, 646, 800, 200);
                menu.setY(bounds.getMaxY() - 200);
                menu.setWidth(bounds.getWidth());
		
		menu.setArcWidth(10);
		menu.setArcHeight(10);
		menu.setFill(gray); // Fill with Gradient

		// Creating the options for our ComboBox with Observable Array List
		ObservableList<String> options = FXCollections.observableArrayList(
				"RED", "PINK", "GREEN", "PURPLE", "BLUE");
		// Create our ComboBox and add the options
		ComboBox<String> comboBox = new ComboBox<String>(options);
		comboBox.setPromptText("Select Spaceship"); // Give instructions to						
		comboBox.setLayoutX((bounds.getMaxX() - 150));                   // the user
		comboBox.setLayoutY(655);
		// Create a Cell Factory to customize our ComboBox
		comboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			public ListCell<String> call(ListView<String> p) {
				final ListCell<String> cell = new ListCell<String>() {
					{
						// Change text Font and size
						super.setPrefWidth(20);
						super.setFont(Font.font("Impact", FontWeight.BOLD, 14));
					}

					@Override
					public void updateItem(String item, boolean empty) {
						// Customize options
						super.updateItem(item, empty);
						if (item != null) {
							setText(item);
							// If ComboBox contains word, add Color
							if (item.contains("RED"))
								setTextFill(Color.ORANGERED);
							else if (item.contains("PINK"))
								setTextFill(Color.DEEPPINK);
							else if (item.contains("GREEN"))
								setTextFill(Color.GREENYELLOW);
							else if (item.contains("PURPLE"))
								setTextFill(Color.PURPLE);
							else
								setTextFill(Color.DEEPSKYBLUE);
						} else
							setText(null);
					}
				};
				return cell;
			}
		});
		// Get userChoice from the ComboBox
		// You need to create a Changelistener of Objects to get value whenever
		// is change.
		comboBox.getSelectionModel().selectedIndexProperty()
				.addListener(new ChangeListener<Object>() {
					public void changed(
							@SuppressWarnings("rawtypes") ObservableValue observable,
							Object oldValue, Object newValue) {
								userChoice = newValue.toString();
							}
		});

		// Create label showing a Instruction to the user
		Label instructionLabel = new Label("Enter a Betting Amount: ");
		instructionLabel.setLayoutX(bounds.getMaxX() / 2);
		instructionLabel.setLayoutY(655);
		// Change font and size
		instructionLabel.setFont(Font.font("Impact", FontWeight.BOLD, 20));

		// This label change depending on the situation
		Label changingLabel = new Label("");
		changingLabel.setLayoutX(bounds.getMaxX() / 2);
		changingLabel.setLayoutY(730);
		changingLabel.setFont(Font.font("Impact", FontWeight.BOLD, 18));

		// Create a text field where user can enter the amount to bet
		TextField textField = new TextField();
		textField.setLayoutX(bounds.getMaxX() / 2);
		textField.setLayoutY(685);
		textField.setFont(Font.font("Impact", FontWeight.BOLD, 20));
		textField.setPromptText("$"); // Display the Dollar Sign($) PromptText

		// TIMER
		// Bind the timerLabel text property to the timeSeconds property
		timerLabel.textProperty().bind(timeSeconds.asString());
		timerLabel.setTextFill(Color.ORANGERED);
		timerLabel.setLayoutX(230);
		timerLabel.setLayoutY(630);
		timerLabel.setFont(Font.font("Impact", FontWeight.BOLD, 150));

		// Create our StartButton
		Button startButton = new Button("Start Race");
		// Create our Reset Button
		Button resetButton = new Button("Reset Race");
		
		// StartButton Properties
		startButton.setFont(Font.font("Impact", FontWeight.BOLD, 31.5));
		startButton.setLayoutX(21);
		startButton.setLayoutY(660);
		// Create a EventHanler so when user Click the Button followings actions
		// going to happen
		startButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						String text = textField.getText(); // Get Text from
														   // TextField
						try {
							// If reset button hasn't been pressed, you can't
							// start a new race
							// Try to convert String to Double
							bet = Double.parseDouble(text);

							if (bet > 1000) // If value is not valid,
											// changingLabel prints...
								changingLabel
										.setText("C'mon! You don't have that much \nmoney. Try again!!");
							else if (bet < 1)
								changingLabel
										.setText("Wrong!! Try we positive values!!");
							else if (userChoice == null) // If not userChoice,
													     // changingLabel
													     // prints...
								changingLabel
										.setText("Choose one Spaceship First!!");
							else {
								// Ok, race is good to go
								// Can't press buttons until race is over
								startButton.setDisable(true);
								resetButton.setDisable(true);
								
								racing = true;

								timeSeconds.set(STARTTIME); // Countdown start
								timeline = new Timeline();
								timeline.getKeyFrames().add(
										new KeyFrame(Duration
										.millis(STARTTIME + 4000), // Timer Duration				
										new KeyValue(timeSeconds, 0))); // Countdown
																		// finish at 0
								timeline.playFromStart(); // Play from beginning

								// Check userChoice and print his/her option.
								switch (userChoice) {
									case "0":
										changingLabel
												.setText("Thanks for Betting!! \n You bet $"
														+ bet + " on the RED Spaceship.");
										break;
									case "1":
										changingLabel
												.setText("Thanks for Betting!! \n You bet $"
														+ bet + " on the PINK Spaceship.");
										break;
									case "2":
										changingLabel
												.setText("Thanks for Betting!! \n You bet $"
														+ bet + " on the GREEN Spaceship.");
										break;
									case "3":
										changingLabel
												.setText("Thanks for Betting!! \n You bet $"
														+ bet + " on the PURPLE Spaceship.");
										break;
									case "4":
										changingLabel
												.setText("Thanks for Betting!! \n You bet $"
														+ bet + " on the BLUE Spaceship.");
										break;
									}
								// Remove old pane
								pane.getChildren().remove(paneRace);
									
								// Create a task to run the Thread that make
								// the rockets
								Runnable race = new MakeRockets(paneRace, finishedOrder);
								threadRace = new Thread(race);
								threadRace.start(); // Start Thread
	
								// Add a new pane
								pane.getChildren().add(paneRace);
	
								// Create a task to run the Thread that post
								// the results of the race
								Runnable results = new PostResults(pane, finishedOrder, 
										changingLabel, userChoice, bet, resetButton);
								threadResult = new Thread(results);
								threadResult.start(); // Start Thread
							}
						} catch (NumberFormatException e) {
							// If there is no bet, changingLabel prints...
							changingLabel
									.setText("C'mon! You need to bet something");
						}
					}
		});

		// ResetButton Properties
		resetButton.setFont(Font.font("Impact", FontWeight.BOLD, 30));
		resetButton.setLayoutX(19);
		resetButton.setLayoutY(730);
		// Create a EventHanler so when user Click the Button followings actions
		// going to happen
		resetButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (racing == false) // If not racing, do nothing
							return;

						// Turn off reset until next race starts
						racing = false;

						// Reset TextField
						textField.setText(null);

						// If user hasn't click Reset race doesn't start
						startButton.setDisable(false);// allow new race to start

						// Reset Timer when Click reset
						if (timeline != null)
							timeline.stop();
						timeSeconds.set(STARTTIME);
						timeline = new Timeline();
						timeline.getKeyFrames().add(
								new KeyFrame(Duration.millis(STARTTIME + 4000),
								new KeyValue(timeSeconds, 0)));

						// Reset Label and queue
						changingLabel.setText("Ok...Make new bet");
						finishedOrder = new LinkedList<String>();
						;

						// Stop the threads from finishing, in case you
						// interrupted a race
						threadRace.interrupt();
						threadResult.interrupt();

						// Clear and Remove old Pane
						paneRace.getChildren().clear();
						pane.getChildren().remove(paneRace);

						// Reset Rockets
						drawRocket(paneRace, -5, -50, 0.6, 360,
								Color.ORANGERED, finishedOrder, "1", false);
						drawRocket(paneRace, -5, 55, 0.6, 360, Color.DEEPPINK,
								finishedOrder, "2", false);
						drawRocket(paneRace, -5, 160, 0.6, 360,
								Color.GREENYELLOW, finishedOrder, "3", false);
						drawRocket(paneRace, -5, 265, 0.6, 360,
								Color.MEDIUMPURPLE, finishedOrder, "4", false);
						drawRocket(paneRace, -5, 375, 0.6, 360,
								Color.DEEPSKYBLUE, finishedOrder, "5", false);

						// Add a new Pane
						pane.getChildren().addAll(paneRace);
					}
		});

		// Add everything to the pane
		pane.getChildren().addAll(menu, comboBox, startButton, resetButton,
				textField, instructionLabel, changingLabel, timerLabel);

		// And then create the scene
                Scene scene2 = new Scene(pane, 800, 600);
                Scene scene1;
		primaryStage.setTitle("Spaceship Race");
                button1.setOnAction(e -> {
                    primaryStage.setScene(scene2);
                    primaryStage.setFullScreen(true);
                    primaryStage.setMaximized(true);
                });
                    
                
                VBox layout1 = new VBox(20);
                layout1.getChildren().addAll(instructions,button1);
                scene1 = new Scene(layout1, 300,200);
		primaryStage.setScene(scene1);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	// Add the Space Background Method
	public static void loadBackground(Pane pane) {
		// Load the image from a specific file
		Image galaxy = new Image("https://newevolutiondesigns.com/images/freebies/galaxy-wallpaper-36.jpg",
				800.0, 800.0, false, true, false);
                
		// Painting the image
		ImageView galaxyBackground = new ImageView();
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                
		galaxyBackground.setImage(galaxy);
                galaxyBackground.setX(bounds.getMinX());
                galaxyBackground.setY(bounds.getMinY());
                galaxyBackground.setFitWidth(bounds.getWidth());
                galaxyBackground.setFitHeight(bounds.getHeight());
               
		// Sets up picture background
		pane.getChildren().add(galaxyBackground);
	}

	// Draw a moon method, with an inserted image
	public static void drawMoon(Pane pane, double centerX, double centerY,
			double scale) {
		// Create our moon with a circle
		Circle moon = new Circle(centerX, centerY, scale);

		// Load the image from a specific URL
		Image moonSurfaceImage = new Image(
				"http://www.iac.es/modacosmica/wp-content/uploads/2015/04/moon.jpg", 275.0 * scale,
				275.0 * scale, false, true, false);

		// Create a image pattern
		ImagePattern imagePatternMoon = new ImagePattern(moonSurfaceImage);

		// Fill our Moon with a Moon Template
		moon.setFill(imagePatternMoon);
                
                //moon.setFill(Color.GRAY);

		// Add it to the pane
		pane.getChildren().add(moon);
	}

	// Draw a sun method with an image
	public static void drawSun(Pane pane, double centerX, double centerY,
			double scale) {
		// Create our sun with a circle
		Circle sun = new Circle(centerX, centerY, scale);

		// Load the image from a specific file
		Image sunSurfaceImage = new Image("http://s3.amazonaws.com/spoonflower/public/design_thumbnails/0063/1604/rr019_Sun_Surface_shop_preview.png",
				8.0 * scale, 8.0 * scale, false, true, false);

		// Create a image pattern
		ImagePattern imagePatternSun = new ImagePattern(sunSurfaceImage);

		// Fill the Sun with a Sun Template
		sun.setFill(imagePatternSun); // It's Hot
                //sun.setFill(Color.YELLOW);

		// Add it to the pane
		pane.getChildren().add(sun);
	}

	// Draw Saturn and rings method
	public static void drawSaturn(Pane pane, double centerX, double centerY,
			double scale, double angle) {
		/*
		 * Radial Gradient to fill the planet, center is to be roughly 1 "radius
		 * length" above the planet or just 2 over the center. Radius is about 4
		 * times planet radius so that it covers properly
		 */
		RadialGradient planetPaint = new RadialGradient(0, 0, centerX, centerY
				- 18 * scale, 35 * scale, false, CycleMethod.NO_CYCLE,
				new Stop[] {
						// We used Color.rgb method for better control over the
						// colors and tones
						new Stop(0.23, Color.rgb(252, 99, 0)),
						new Stop(0.25, Color.DARKGOLDENROD),
						new Stop(0.27, Color.rgb(220, 170, 40)),
						new Stop(0.29, Color.rgb(255, 179, 50)),
						new Stop(0.3, Color.rgb(180, 72, 31)),
						new Stop(0.35, Color.rgb(136, 87, 29)),
						new Stop(0.36, Color.rgb(227, 170, 100)),
						new Stop(0.37, Color.rgb(255, 179, 83)),
						new Stop(0.4, Color.rgb(200, 140, 135)),
						new Stop(0.455, Color.rgb(255, 179, 83)),
						new Stop(0.47, Color.rgb(227, 170, 100)),
						new Stop(0.48, Color.rgb(255, 179, 83)),
						new Stop(0.5, Color.rgb(249, 98, 0)),
						new Stop(0.53, Color.rgb(255, 179, 83)),
						new Stop(0.55, Color.rgb(227, 170, 100)),
						new Stop(0.6, Color.rgb(255, 179, 83)),
						new Stop(0.62, Color.rgb(227, 170, 100)),
						new Stop(0.64, Color.rgb(255, 179, 83)),
						new Stop(0.65, Color.rgb(227, 170, 100)),
						new Stop(0.67, Color.rgb(255, 179, 83)),
						new Stop(0.68, Color.rgb(227, 170, 100)),
						new Stop(0.7, Color.rgb(255, 179, 83)),
						new Stop(0.72, Color.rgb(230, 180, 160)),
						new Stop(0.75, Color.rgb(145, 107, 62)) });

		// Draw Saturn
		Circle planetBody = new Circle(centerX, centerY, 10 * scale);
		planetBody.setFill(planetPaint);

		// Made methods to draw the rings... more details down
		Shape ring1Done = drawArc(centerX, centerY, 13 * scale, 3 * scale,
				scale, scale / 2, Color.DARKGOLDENROD);

		Shape ring2Done = drawArc(centerX, centerY, 14 * scale, 4 * scale,
				scale, scale / 2, Color.DARKSALMON);

		Shape ring3Done = drawArc(centerX, centerY, 15 * scale, 5 * scale,
				scale, scale / 2, Color.CHOCOLATE);

		Shape ring4Done = drawArc(centerX, centerY, 16 * scale, 6 * scale,
				scale, scale / 2, Color.rgb(110, 33, 3));

		Shape ring5Done = drawArc(centerX, centerY, 13 * scale, 3 * scale,
				scale, scale, Color.BEIGE);

		Shape ring6Done = drawArc(centerX, centerY, 14 * scale, 4 * scale,
				scale, scale, Color.BURLYWOOD);

		Shape ring7Done = drawArc(centerX, centerY, 15 * scale, 5 * scale,
				scale, scale, Color.DARKGOLDENROD);

		Shape ring8Done = drawArc(centerX, centerY, 16 * scale, 6 * scale,
				scale, scale, Color.rgb(149, 68, 5));

		// Auxiliary pane for rotation purposes
		Pane aux = new Pane();
		aux.getChildren().addAll(planetBody, ring5Done, ring1Done, ring6Done,
				ring2Done, ring7Done, ring3Done, ring8Done, ring4Done);
		aux.setRotate(angle); // Rotate everything together

		// Now add the aux pane after rotation
		pane.getChildren().add(aux);
	}

	/*
	 * Helper method to make the rings, it takes Double center X and Y, same as
	 * the original planet, radius X and Y to make the elipse, a scale (to scale
	 * with the planet) a width for the stroke width, and a color to fill it in
	 * the end.
	 */

	private static Shape drawArc(Double centerX, Double centerY,
			Double radiusX, Double radiusY, Double scale, Double width,
			Color color) {
		// The "half planet" to substract from rings
		Arc planetBodyTop = new Arc(centerX, centerY, 10 * scale, 10 * scale,
				0, 180);
		planetBodyTop.setFill(Color.BLACK); // It still needs to be filled

		// The full ring
		Ellipse ring = new Ellipse(centerX, centerY, radiusX, radiusY);
		ring.setFill(null); // No fill
		ring.setStrokeWidth(width); // However it does have stroke
		ring.setStroke(Color.BLACK);

		// Final shape is just subtracting planetTop from the ring
		Shape ringDone = Shape.subtract(ring, planetBodyTop);
		ringDone.setFill(color); // Color accordingly

		return ringDone; // And finally return the resulting finished shape
	}

	// Draw a rocket method
	public static void drawRocket(Group pane, double centerX, double centerY,
			double scale, double angle, Color stripesColor,
			Queue<String> finishOrder, String rocketNum, boolean runing) {
		// Filling our Rocket with Gradient
		// Choose two Colors
		Stop[] paintGradientRocket = new Stop[] { new Stop(0, Color.GRAY),
				new Stop(1, Color.SILVER) };
		Stop[] paintGradientFire = new Stop[] { new Stop(0, Color.RED),
				new Stop(1, Color.YELLOW) };
		Stop[] paintGradientWings = new Stop[] { new Stop(0, Color.MEDIUMBLUE),
				new Stop(1, Color.DEEPSKYBLUE) };

		// Fills with a linear color gradient pattern
		LinearGradient gray = new LinearGradient(0, 0, 1, 0, true,
				CycleMethod.NO_CYCLE, paintGradientRocket);
		LinearGradient red = new LinearGradient(0, 0, 1, 0, true,
				CycleMethod.NO_CYCLE, paintGradientFire);
		LinearGradient blue = new LinearGradient(0, 0, 1, 0, true,
				CycleMethod.NO_CYCLE, paintGradientWings);

		// Draw the wings. They are just one arc made to be almost a full
		// ellipse
		Arc wings = new Arc(centerX + 160 * scale, centerY + 190 * scale,
				70 * scale, 60 * scale, 220, 280);
		wings.setType(ArcType.ROUND);
		wings.setFill(blue); // Add Gradient Color to the wings
		wings.setStroke(Color.DARKBLUE);
		wings.setStrokeWidth(10 * scale);
		wings.setStrokeType(StrokeType.INSIDE);

		// Rocket body is made out of 3 pieces
		// First one elliptic arc up
		Arc bodySideUp = new Arc(centerX + 200 * scale, centerY + 200 * scale,
				100 * scale, 50 * scale, 10, 130);
		bodySideUp.setStroke(null);
		bodySideUp.setType(ArcType.ROUND);

		// Then one elliptic arc down
		Arc bodySideDown = new Arc(centerX + 200 * scale,
				centerY + 182 * scale, 100 * scale, 50 * scale, 220, 130);
		bodySideDown.setStroke(null);
		bodySideDown.setType(ArcType.ROUND);

		// Then a rectangle for the back and to fill body
		Rectangle fillRocket = new Rectangle(centerX + 123 * scale, centerY
				+ 168 * scale, 60 * scale, 46 * scale);

		// For commodity, we used union to merge all 3 into one single shape
		Shape rocketBody = Shape.union(bodySideDown,
				Shape.union(bodySideUp, fillRocket));
		rocketBody.setFill(gray); // Add Gradient Color to our Rocket

		// This draw a nice red rocket head... It wouldn't be a rocket otherwise
		Arc rocketHead = new Arc(centerX + 310 * scale, centerY + 190 * scale,
				20 * scale, 10 * scale, 150, 60);
		rocketHead.setType(ArcType.ROUND);
		rocketHead.setFill(stripesColor);
		rocketHead.setStroke(stripesColor);
		rocketHead.setStrokeWidth(10);

		// Stripes...stripes are cool too. We made it with half an arc
		Arc rocketStripe = new Arc(centerX + 310 * scale,
				centerY + 190 * scale, 40 * scale, 35 * scale, 120, 120);
		rocketStripe.setType(ArcType.OPEN);
		rocketStripe.setFill(null);
		rocketStripe.setStrokeWidth(5 * scale);
		rocketStripe.setStroke(stripesColor);

		// We use Shape.intersect for better results, making the body a single
		// shape
		Shape finalRocketStripe = Shape.intersect(rocketStripe, rocketBody);
		finalRocketStripe.setFill(stripesColor);

		// It can have many stripes
		Arc rocketStripe2 = new Arc(centerX + 310 * scale, centerY + 190
				* scale, 150 * scale, 100 * scale, 120, 120);
		rocketStripe2.setType(ArcType.OPEN);
		rocketStripe2.setFill(null);
		rocketStripe2.setStrokeWidth(12 * scale);
		rocketStripe2.setStroke(stripesColor);

		// We used Shape.intersect again
		Shape finalRocketStripe2 = Shape.intersect(rocketStripe2, rocketBody);
		finalRocketStripe2.setFill(stripesColor);

		// Draw the back of the rocket with a line
		Line rocketBack = new Line(centerX + 124 * scale,
				centerY + 168 * scale, centerX + 124 * scale, centerY + 214
						* scale);
		rocketBack.setStrokeWidth(6 * scale);

		// Then we added it to to the final shape
		Shape finalRocketBody = Shape.intersect(rocketBack, rocketBody);
		finalRocketBody.setFill(Color.SILVER);

		// The fire that pushes the rocket, made with a Polyline Shape
		Polyline fire = new Polyline();
		fire.getPoints().addAll(
				new Double[] { centerX + 123.5 * scale,
						centerY + 168.0 * scale, centerX + 90.0 * scale,
						centerY + 150.0 * scale, centerX + 100.0 * scale,
						centerY + 168.0 * scale, centerX + 70.0 * scale,
						centerY + 160.0 * scale, centerX + 80.0 * scale,
						centerY + 180.0 * scale, centerX + 60.0 * scale,
						centerY + 190.0 * scale, centerX + 80.0 * scale,
						centerY + 200.0 * scale, centerX + 70.0 * scale,
						centerY + 220.0 * scale, centerX + 100.0 * scale,
						centerY + 213.0 * scale, centerX + 90.0 * scale,
						centerY + 231.0 * scale, centerX + 123.5 * scale,
						centerY + 213.0 * scale });

		fire.setStroke(Color.RED);
		fire.setFill(red);
		fire.setStrokeWidth(6 * scale);
		fire.setStrokeType(StrokeType.INSIDE);

		// Small window, if you are up there, might as well look at the stars
		Circle window = new Circle(centerX + 220 * scale,
				centerY + 190 * scale, 25 * scale, Color.AQUA);

		// Windows edge, something got to hold it
		Circle windowEdge = new Circle(centerX + 220 * scale, centerY + 190
				* scale, 25 * scale, null);
		windowEdge.setStroke(Color.BLACK);
		windowEdge.setStrokeWidth(5 * scale);
		windowEdge.setStrokeType(StrokeType.OUTSIDE);

		// The screws that hold it together, made the "screw"
		// sense by making the stroke a dash line.
		Circle windowScrews = new Circle(centerX + 220 * scale, centerY + 190
				* scale, 28 * scale, null);
		windowScrews.setStrokeWidth(2 * scale);
		// Made the dash intervals depending on the circle radius, this way it
		// can be scaled, and still have the same number of screws...also makes
		// them equidistant
		windowScrews.getStrokeDashArray().addAll(0d, (28 * scale * 3.14) / 10);
		windowScrews.setStrokeLineCap(StrokeLineCap.ROUND);
		windowScrews.setStroke(Color.SILVER); // Silver is flashy

		// Again an auxiliary Pane for rotation
		Pane aux = new Pane();

		// Order is important, add all the rocket body up to window, then the
		// Stitch, then the window edge and holder. This helps covering stitch
		// without need to go intersecting and subtracting shapes
		aux.getChildren().addAll(wings, fire, rocketBody, rocketHead, window,
				finalRocketStripe, finalRocketStripe2, finalRocketBody);
		// Call Stitch method
		drawStitch(aux, centerX + 190 * scale, centerY + 160 * scale,
				1.5 * scale, Color.CORNFLOWERBLUE, Color.DARKBLUE,
				Color.LIGHTBLUE);
		// Add shapes to cover Stitch
		aux.getChildren().addAll(windowEdge, windowScrews);
		aux.setRotate(angle); // Rotate aux
                
                Screen screen = Screen.getPrimary();
                
                Rectangle2D bounds = screen.getVisualBounds();
                

		// Path Transition, only if it is going to race
		if (runing) {
			// Create a path
			Path path = new Path();
			path.getElements().add(new MoveTo(50, 80 + centerY * 0.5));// where the problem may be
			path.getElements().add(new LineTo(bounds.getWidth() - 100, 80 + centerY * 0.5));
			path.setVisible(false);

			// Create a PathTransition
			PathTransition pathTransition = new PathTransition();
			// Duration of the race is Random, so the rockets go in difference
			// speed
			pathTransition.setDuration(Duration.millis(Math.random() * 2000 + 5000));
			pathTransition.setPath(path); // Set path to follow
			pathTransition.setNode(aux); // Set Rocket as the Node
			pathTransition
					.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
			pathTransition.play(); // Start Animation
			// Create a EventHandler to know who finished the race and in what position.
			pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// When Rocket finish race get position
					finishOrder.add(rocketNum);
				}
			});
			pane.getChildren().add(path);
		}
		pane.getChildren().add(aux);
	}

	// Draw the Stitch method, only to mid body
	public static void drawStitch(Pane pane, double centerX, double centerY,
			double scale, Color bodyColor, Color darkBody, Color fill) {
		// Draw Stitch's head
		Circle head = new Circle(centerX + 20 * scale, centerY + 20 * scale,
				10 * scale);
		head.setFill(bodyColor); // Pass color in case you want to change it

		// Made Stitch's hair with a polygon
		Polygon hair = new Polygon();
		hair.setFill(bodyColor);
		hair.getPoints().addAll(
				new Double[] { centerX + 18.0 * scale, centerY + 12.0 * scale,
						centerX + 22.0 * scale, centerY + 12.0 * scale,
						centerX + 22.0 * scale, centerY + 9.0 * scale,
						centerX + 21.0 * scale, centerY + 10.0 * scale,
						centerX + 20.0 * scale, centerY + 9.0 * scale,
						centerX + 19.0 * scale, centerY + 10.0 * scale,
						centerX + 18.0 * scale, centerY + 9.0 * scale });

		// Both ears, a polyline
		Polyline leftEar = new Polyline();
		leftEar.getPoints().addAll(
				new Double[] { centerX + 14.0 * scale, centerY + 19.0 * scale,
						centerX + 10.0 * scale, centerY + 14.0 * scale,
						centerX + 8.0 * scale, centerY + 9.0 * scale,
						centerX + 5.0 * scale, centerY + 8.0 * scale,
						centerX + 4.0 * scale, centerY + 10.0 * scale,
						centerX + 5.0 * scale, centerY + 14.0 * scale,
						centerX + 6.0 * scale, centerY + 16.5 * scale,
						centerX + 12.0 * scale, centerY + 24.0 * scale });
		leftEar.setStrokeLineCap(StrokeLineCap.ROUND); // Round corners
		leftEar.setStroke(Color.LIGHTPINK); // Pass earColors in case you want
		leftEar.setFill(Color.LIGHTPINK); // to change it
		leftEar.setStrokeWidth(1);

		Polyline rightEar = new Polyline();
		rightEar.getPoints().addAll(
				new Double[] { centerX + 26.0 * scale, centerY + 19.0 * scale,
						centerX + 30.0 * scale, centerY + 14.0 * scale,
						centerX + 32.0 * scale, centerY + 9.0 * scale,
						centerX + 35.0 * scale, centerY + 8.0 * scale,
						centerX + 36.0 * scale, centerY + 10.0 * scale,
						centerX + 35.0 * scale, centerY + 14.0 * scale,
						centerX + 34.0 * scale, centerY + 16.5 * scale,
						centerX + 28.0 * scale, centerY + 24.0 * scale });
		rightEar.setStrokeLineCap(StrokeLineCap.ROUND); // Round corners
		rightEar.setStroke(Color.LIGHTPINK); // Pass earColors in case you want
		rightEar.setFill(Color.LIGHTPINK); // to change it
		rightEar.setStrokeWidth(1);

		// EYES
		// Helpers radiuses
		double xRadius = 3.0 * scale;
		double yRadius = 2.0 * scale;

		// Eyes are ellipses rotated. First the back of the eye, then the eye,
		// and then a small white circle for pupil.
		Circle pupilLeft = new Circle(centerX + 15 * scale, centerY + 20
				* scale, 0.8 * scale);
		pupilLeft.setFill(Color.WHITE);

		Ellipse eyeLeft = new Ellipse(centerX + 16 * scale, centerY + 21.5
				* scale, xRadius, yRadius);
		eyeLeft.setFill(Color.BLACK);
		eyeLeft.setRotate(135 / 2.0);

		Ellipse eyeLeftBack = new Ellipse(centerX + 15 * scale, centerY + 20
				* scale, xRadius * 1.5, yRadius * 1.5);
		eyeLeftBack.setFill(fill);
		eyeLeftBack.setRotate(135 / 2.0);

		Circle pupilRight = new Circle(centerX + 25 * scale, centerY + 20
				* scale, 0.8 * scale);
		pupilRight.setFill(Color.WHITE);

		Ellipse eyeRight = new Ellipse(centerX + 24 * scale, centerY + 21.5
				* scale, xRadius, yRadius);
		eyeRight.setFill(Color.BLACK);
		eyeRight.setRotate(225 / 2.0);

		Ellipse eyeRightBack = new Ellipse(centerX + 25 * scale, centerY + 20
				* scale, xRadius * 1.5, yRadius * 1.5);
		eyeRightBack.setFill(fill);
		eyeRightBack.setRotate(225 / 2.0);

		// NOSE, made with two ellipses
		double xNoseRadius = 2 * scale;
		double yNoseRadius = 1 * scale;

		Ellipse noseLength = new Ellipse(centerX + 20 * scale, centerY + 26
				* scale, xNoseRadius, yNoseRadius * 1.5);
		noseLength.setFill(darkBody);
		noseLength.setRotate(180 / 2.0);

		Ellipse noseWidth = new Ellipse(centerX + 20 * scale, centerY + 25
				* scale, xNoseRadius * 1.5, yNoseRadius * 1.5);
		noseWidth.setFill(darkBody);
		noseWidth.setRotate(360 / 2.0);

		// Stitch's hands are ellipses rotated
		Ellipse leftHand = new Ellipse(centerX + 15 * scale, centerY + 32
				* scale, xRadius * 1.5, yRadius * 1.5);
		leftHand.setFill(bodyColor);
		leftHand.setStroke(Color.BLACK);
		leftHand.setStrokeWidth(1);
		leftHand.setRotate(325 / 2.0);

		Ellipse rightHand = new Ellipse(centerX + 25 * scale, centerY + 32
				* scale, xRadius * 1.5, yRadius * 1.5);
		rightHand.setFill(bodyColor);
		rightHand.setStroke(Color.BLACK);
		rightHand.setStrokeWidth(1);
		rightHand.setRotate(45 / 2.0);

		// Body is made with rectangles with rounded edges
		Rectangle body = new Rectangle(centerX + 12 * scale, centerY + 28
				* scale, 16 * scale, 8 * scale);
		body.setArcWidth(8 * scale);
		body.setArcHeight(8 * scale);
		body.setFill(bodyColor);

		Rectangle insideBody = new Rectangle(centerX + 15 * scale, centerY + 28
				* scale, 10 * scale, 9 * scale);
		insideBody.setArcWidth(8 * scale);
		insideBody.setArcHeight(8 * scale);
		insideBody.setFill(fill); // Option to change color

		// Finally, add it all in the proper order
		pane.getChildren().addAll(body, insideBody, leftEar, rightEar, head,
				eyeRightBack, eyeRight, eyeLeftBack, eyeLeft, pupilLeft,
				pupilRight, noseLength, noseWidth, hair, rightHand, leftHand);
	}

	public static void drawTrack(Pane pane) {
		// Nested Loops to make our track
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                        
		for (int i = 0; i < 80; i++) {
			for (int j = 0; j < 6; j++) 
                        {
				// i is the number of stars
				// j is the number of lines
				// Create a star with Polygon
				Polygon star = new Polygon();
				star.getPoints()
						.addAll(new Double[] { 15.00 + i * 21, 12.0 + j * 105,
								13.05 + i * 21, 16.5 + j * 105, 9.00 + i * 21,
								16.5 + j * 105, 12.0 + i * 21, 19.5 + j * 105,
								10.5 + i * 21, 24.0 + j * 105, 15.0 + i * 21,
								21.75 + j * 105, 19.5 + i * 21, 24.0 + j * 105,
								18.0 + i * 21, 19.5 + j * 105, 21.0 + i * 21,
								16.5 + j * 105, 17.1 + i * 21, 16.5 + j * 105, });
				star.setRotate(i * 10); // Rotate start every loop
				star.setFill(Color.GHOSTWHITE); // Add Color
				pane.getChildren().add(star); // Add it to the pane

				// Create a FadeTransition
				// It looks like the stars are moving 
				FadeTransition ft = new FadeTransition(Duration.millis(1000), 
						star); // Fade Duration
				ft.setFromValue(1.0);
				ft.setToValue(0.1);
				ft.setCycleCount(Timeline.INDEFINITE); // Infinite Fade
				ft.setAutoReverse(false); // Start from Dark to light Color
				ft.play(); // Play FadeTransition
			}
		}
	}

	// The task that prints the results of the race
	class PostResults implements Runnable {
		Queue<String> finishedOrder; // Race Positions
		Label changingLabel; // ChangingLabel
		Pane pane; // Our pane
		String userChoice; // User ComboBox selection
		double bet; // User Bet amount
		Button resetButton; // Reset Button

		// Construct a task with specific values
		public PostResults(Pane pane, Queue<String> finishedOrder,
				Label changingLabel, String userChoice, double bet, Button resetButton) {
			this.finishedOrder = finishedOrder;
			this.pane = pane;
			this.changingLabel = changingLabel;
			this.userChoice = userChoice;
			this.bet = bet;
			this.resetButton = resetButton;
		}

		// Tell Thread how to run
		public void run() {
			try {
				while (finishedOrder.size() < 5)
					// Put task to sleep until all 5 rockets finish the race
					Thread.sleep(500);
			} catch (InterruptedException e) {
				return; // If sleep is interrupted it was due to reset,
			}
			
			String temp = ""; // Text to Display
			
			if (finishedOrder.poll().equals(userChoice)) // If userChoice finished 1rst...
				temp = ("Wow, You Just Won First!! You Earned $" + bet * 1000 + "!!!");
			else if (finishedOrder.poll().equals(userChoice)) // If userChoice finished 2nd...
				temp = ("Nice, You Got Second. You Earned $" + bet * 500 + "!!");
			else if (finishedOrder.poll().equals(userChoice)) // If userChoice finished 3rd...
				temp = ("Well, 3rd is better than none. You Earned $" + bet
						* 250 + "!");
			else // If userChoice finished 4th or last...
				temp = ("Nope, Better Luck Next Time!!");

			final String result = temp;

			// Run after rockets finished the race
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// Set text giving results
					changingLabel.setText(result);
					// Can't Click reset until race is finish
					resetButton.setDisable(false);
				}
			});
		}
	}

	// The task that draw the rockets
	class MakeRockets implements Runnable {
		Queue<String> finishedOrder; // Race Positions
		Group pane; // Our Pane

		// Construct a task with specific values
		public MakeRockets(Group pane, Queue<String> finishedOrder) {
			this.finishedOrder = finishedOrder;
			this.pane = pane;
		}

		// Tell Thread how to run
		public void run() {
			try {
				// Put task to sleep for the specific time in milliseconds
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				return; // if sleep is interrupted it was due to reset,			
			}           // and in that case the thread should just stop
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					pane.getChildren().clear(); // Clear old pane
					                            // Draw rockets
					drawRocket(pane, -5, -50, 0.6, 360, Color.ORANGERED,
							finishedOrder, "0", true);
					drawRocket(pane, -5, 55, 0.6, 360, Color.DEEPPINK,
							finishedOrder, "1", true);
					drawRocket(pane, -5, 160, 0.6, 360, Color.GREENYELLOW,
							finishedOrder, "2", true);
					drawRocket(pane, -5, 265, 0.6, 360, Color.MEDIUMPURPLE,
							finishedOrder, "3", true);
					drawRocket(pane, -5, 375, 0.6, 360, Color.DEEPSKYBLUE,
							finishedOrder, "4", true);
				}
			});
		}
	}
}

