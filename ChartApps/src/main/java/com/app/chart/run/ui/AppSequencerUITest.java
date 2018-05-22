/**
 * 
 */
package com.app.chart.run.ui;

import java.io.File;

import com.app.chart.dashboard.ui.PerfomanceBoardDetails;
import com.app.chart.fx.FilesUtil;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Sandeep
 *
 */
public class AppSequencerUITest extends Application {

	public static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	public static double WIDTH = visualBounds.getWidth() - 100;
	public static double HEIGHT = visualBounds.getHeight() - 50;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setMinHeight(HEIGHT);
		stage.setMinWidth(WIDTH);

		Scene scene = new Scene(new AppSequencerUI(""), WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.show();

	}

	public static void main(String args[]) {
		launch(args);
	}
}
