package com.app.chart.model.test;

import javafx.application.Application;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ReflectionEffectExample extends Application {
	@Override
	public void start(Stage stage) {
		// Creating a Text object
		Text text = new Text();

		// Setting font to the text
		text.setFont(Font.font(null, FontWeight.BOLD, 40));
		Image image = new Image("http://www.tutorialspoint.com/green/images/logo.png");

		// Setting the image view
		ImageView imageView = new ImageView(image);

		// setting the fit width of the image view
		imageView.setFitWidth(100);

		// Setting the preserve ratio of the image view
		imageView.setPreserveRatio(true);

		// Instantiating the Glow class
		Glow glow = new Glow();

		// setting level of the glow effect
		glow.setLevel(100);

		// Applying bloom effect to text
		imageView.setEffect(glow);

		// setting the position of the text
		//text.setX(60);
		//text.setY(100);

		// Setting the text to be embedded.
		text.setText("Welcome to Tutorialspoint");

		// Setting the color of the text
		text.setFill(Color.TEAL);
		text.setTextAlignment(TextAlignment.CENTER);

		// Instanting the reflection class
		Reflection reflection = new Reflection();

		// setting the bottom opacity of the reflection
		reflection.setBottomOpacity(0.0);

		// setting the top opacity of the reflection
		reflection.setTopOpacity(0.5);

		// setting the top offset of the reflection
		reflection.setTopOffset(0.0);

		// Setting the fraction of the reflection
		reflection.setFraction(0.7);

		// Applying reflection effect to the text
		//text.setEffect(reflection);

		// Creating a Group object
		HBox box = new HBox(5);
		
		
		HBox box1 = new HBox(5);
		box1.getChildren().addAll(imageView);
		
		HBox box2 = new HBox();
		//box2.setAlignment(Pos.CENTER);
		box2.getChildren().add(text);
		
		box.getChildren().addAll(box1, box2);
		
		/*Group root = new Group(imageView, text);
		root.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);*/

		// Creating a scene object
		Scene scene = new Scene(box, 1600, 300);

		// Setting title to the Stage
		stage.setTitle("Reflection effect example");

		// Adding scene to the stage
		stage.setScene(scene);

		// Displaying the contents of the stage
		stage.show();
	}

	public static void main(String args[]) {
		launch(args);
	}
}