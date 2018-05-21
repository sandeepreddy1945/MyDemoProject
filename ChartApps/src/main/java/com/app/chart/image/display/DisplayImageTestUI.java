/**
 * 
 */
package com.app.chart.image.display;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Sandeep Reddy Battula
 *
 */
public class DisplayImageTestUI extends Application {

	public static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	public static double WIDTH = visualBounds.getWidth();
	public static double HEIGHT = visualBounds.getHeight();

	@Override
	public void start(Stage stage) throws Exception {
		stage.setMinHeight(HEIGHT);
		stage.setMinWidth(WIDTH);
		stage.setFullScreen(true);

		Scene scene = new Scene(new DisplayImage("sample.png", true, "Sandeep Reddy Battula"), WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.show();

	}

	public static void main(String args[]) {
		launch(args);
	}

}
