/**
 * 
 */
package com.app.chart.customer.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Sandeep
 *
 */
public class CustomerUITest extends Application {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws Exception {
		stage.setMaximized(true);

		Scene scene = new Scene(new CustomerUI(), 1200, 800);

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
