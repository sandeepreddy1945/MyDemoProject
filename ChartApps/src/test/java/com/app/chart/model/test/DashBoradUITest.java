/**
 * 
 */
package com.app.chart.model.test;

import org.kordamp.bootstrapfx.scene.layout.Panel;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @author Sandeep
 *
 */
public class DashBoradUITest extends Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage stage) throws Exception {
		Panel panel = new Panel("Sample Panel");

		ImageView imageView = new ImageView(
				ClassLoader.getSystemResource("com/app/chart/images/2.jpg").toExternalForm());
		// Setting the position of the image
		imageView.setX(50);
		imageView.setY(25);

		// setting the fit height and width of the image view
		imageView.setFitHeight(455);
		imageView.setFitWidth(500);

		// Setting the preserve ratio of the image view
		imageView.setPreserveRatio(true);
		panel.setMaxSize(100, 50);
		panel.setMinSize(100, 50);
		panel.setBody(imageView);
		Group root = new Group(imageView);  

		Scene scene = new Scene(root, 500, 500);
		stage.setScene(scene);
		stage.show();

	}

}
