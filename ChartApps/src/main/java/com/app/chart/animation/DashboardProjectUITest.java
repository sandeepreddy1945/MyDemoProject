/**
 * 
 */
package com.app.chart.animation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Sandeep
 *
 */
public class DashboardProjectUITest extends Application {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(new DashboardProjectStatus(), 1200, 1000);

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
