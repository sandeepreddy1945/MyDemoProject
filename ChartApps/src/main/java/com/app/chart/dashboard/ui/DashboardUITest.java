/**
 * 
 */
package com.app.chart.dashboard.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Sandeep
 *
 */
public class DashboardUITest extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.setMinHeight(1000);
		stage.setMinWidth(1400);

		Scene scene = new Scene(new ReleaseBoardDetails(), 1300, 900);
		stage.setScene(scene);
		stage.show();

	}

	public static void main(String args[]) {
		launch(args);
	}
}
