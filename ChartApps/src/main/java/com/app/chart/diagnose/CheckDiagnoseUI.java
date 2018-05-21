/**
 * 
 */
package com.app.chart.diagnose;

import java.io.File;

import com.app.chart.fx.FilesUtil;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Sandeep Reddy Battula
 *
 */
public class CheckDiagnoseUI extends Application {

	public static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	public static double WIDTH = visualBounds.getWidth() - 100;
	public static double HEIGHT = visualBounds.getHeight() - 50;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setMinHeight(HEIGHT);
		stage.setMinWidth(WIDTH);

		Scene scene = new Scene(new DiagnoseIssues(new File(FilesUtil.DASHBOARD_CONTENT_DATA)), WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.show();

	}

}
