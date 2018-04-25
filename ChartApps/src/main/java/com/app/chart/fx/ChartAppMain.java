/**
 * 
 */
package com.app.chart.fx;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author ezibcef
 *
 */
public class ChartAppMain extends Application {

	public static final String SLASH = "/";
	public static final String MPS_CHARTS_PATH = "MPS-Charts";
	public static final String MAIN_APP_PATH = FileUtils.getUserDirectory().getAbsolutePath() + SLASH + MPS_CHARTS_PATH;
	public static final String IMAGES_DIR_PATH = FileUtils.getUserDirectory().getAbsolutePath() + SLASH
			+ MPS_CHARTS_PATH + SLASH + "images";
	public static final String HTML_DIR_PATH = FileUtils.getUserDirectory().getAbsolutePath() + SLASH + MPS_CHARTS_PATH
			+ SLASH + "html";
	public static final String PROPS_DIR_PATH = FileUtils.getUserDirectory().getAbsolutePath() + SLASH + MPS_CHARTS_PATH
			+ SLASH + "properties";
	public static final String JSON_MSGS_PATH = FileUtils.getUserDirectory().getAbsolutePath() + SLASH + MPS_CHARTS_PATH
			+ SLASH + "jsons";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//initilize the file folders
		initializeFileSettings();
		
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

	/**
	 * Initialize the Dirs ..In the User Directory for App to Fucntion
	 */
	private void initializeFileSettings() {
		checkAndCreateDir(MAIN_APP_PATH, IMAGES_DIR_PATH, HTML_DIR_PATH, PROPS_DIR_PATH, JSON_MSGS_PATH);
	}

	/**
	 * Make Dir Based On The Paths
	 * @param files
	 */
	private void checkAndCreateDir(String... files) {

		Arrays.asList(files).stream().filter(f -> !new File(f).exists()).forEach(f -> new File(f).mkdir());
	}
}
