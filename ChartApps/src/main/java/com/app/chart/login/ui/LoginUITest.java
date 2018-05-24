/**
 * 
 */
package com.app.chart.login.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author Sandeep Reddy Battula
 *
 */
public class LoginUITest extends Application {

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) throws Exception {
		stage.setMaximized(false);
		stage.setMinHeight(400);
		stage.setMinWidth(500);
		AnchorPane toolbar = FXMLLoader
				.load(getClass().getResource("/com/app/chart/login/ui/login.fxml"));
		
		Scene scene = new Scene(toolbar, 400, 250);
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
