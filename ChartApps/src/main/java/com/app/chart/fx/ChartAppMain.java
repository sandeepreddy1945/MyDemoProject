/**
 * 
 */
package com.app.chart.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author ezibcef
 *
 */
public class ChartAppMain extends Application {

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// initilize the file folders
		FilesUtil.initializeFileSettings();

		// set the app to full screen mode
		primaryStage.setFullScreen(true);
		primaryStage.setMaximized(true);
		primaryStage.setMinWidth(600);
		primaryStage.setMinHeight(600);
		primaryStage.setResizable(false);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setTitle("MPS - Project Details");

		// initialize the chart web engine
		ChartWebEngine chartWebEngine = new ChartWebEngine().initialize();

		Scene scene = new Scene(chartWebEngine.getWebView(), primaryStage.getMaxWidth(), primaryStage.getMaxHeight());
		primaryStage.setScene(scene);
		Platform.runLater(() -> primaryStage.show());
		// start the timer
		chartWebEngine.getAnimationTimer().start();
	}

	
}
