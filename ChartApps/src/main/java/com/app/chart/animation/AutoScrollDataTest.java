/**
 * 
 */
package com.app.chart.animation;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Sandeep
 *
 */
public class AutoScrollDataTest extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		stage.setMaximized(true);

		Scene scene = new Scene(
				new AutoScrollData1(FXCollections.observableArrayList("sandeep", "reddy", "battula","sita ram"), 1200), 1200, 200);

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
