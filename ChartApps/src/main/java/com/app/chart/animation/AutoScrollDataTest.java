/**
 * 
 */
package com.app.chart.animation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Sandeep
 *
 */
public class AutoScrollDataTest extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		//stage.setMaximized(true);

		Scene scene = new Scene(
				new AutoScrollProjectStatus( Screen.getPrimary().getVisualBounds().getHeight()  - 150), 150,  Screen.getPrimary().getVisualBounds().getHeight()  - 150);

		stage.setScene(scene);
		stage.show();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}

}
