/**
 * 
 */
package com.app.chart.fx;

import java.io.File;

import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.app.chart.perfomance.dashboard.ui.DashboardUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
		primaryStage.setMinWidth(1200);
		primaryStage.setMinHeight(1000);
		primaryStage.setResizable(true);
		primaryStage.setAlwaysOnTop(false);
		primaryStage.setTitle("MPS - Project Details");

		// initialize the chart web engine
		ChartWebEngine chartWebEngine = new ChartWebEngine().initialize();
		// set the primary stage object to webview for popup displays
		chartWebEngine.setParenStage(primaryStage);

		HBox box = new HBox();
		VBox mainBox = new VBox(5);
		// TODO some how add the header name here dynamically.
		mainBox.getChildren().add(DashboardUtil.HeaderSegment(box, ""));
		chartWebEngine.getWebView().setPrefSize(DashboardUI.WIDTH, DashboardUI.HEIGHT - 60);
		chartWebEngine.getWebView().setMinSize(DashboardUI.WIDTH - 160, DashboardUI.HEIGHT - 110);
		chartWebEngine.getWebView().setTranslateZ(10);
		mainBox.getChildren().add(chartWebEngine.getWebView());
		HBox footerSegment = DashboardUtil.FooterSegment();
		footerSegment.setPrefHeight(25);
		mainBox.getChildren().add(footerSegment);

		box.getChildren().add(mainBox);
		Scene scene = new Scene(box, primaryStage.getMaxWidth(), primaryStage.getMaxHeight());
		primaryStage.setScene(scene);
		primaryStage.show();

		// display the screen after data shows up
		File chartFile = new File("D:\\Study\\crackerEffects\\example\\app.html");
		chartWebEngine.displayData(chartFile.toURI().toURL().toExternalForm());

		// start the timer
		// chartWebEngine.getAnimationTimer().start();
	}

}
