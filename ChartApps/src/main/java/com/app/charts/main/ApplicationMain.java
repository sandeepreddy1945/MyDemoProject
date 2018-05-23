/**
 * 
 */
package com.app.charts.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Sandeep Reddy Battula
 *
 *         <b> Main Class File The Application Runs on .</b> <br>
 *         Important that this class should be left untouched . Touching this
 *         class methods would break the application or effect its
 *         performance.<br>
 */
public class ApplicationMain extends Application {

	/**
	 * A time Line task with a delay of 2 minutes for each slide.
	 */
	Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(2.0), this::executeTask));

	@Override
	public void init() throws Exception {
	}

	@Override
	public void start(Stage stage) throws Exception {

	}

	@Override
	public void stop() throws Exception {
	}

	public void executeTask(ActionEvent e) {

	}

	public static void main(String[] args) {
		launch(args);
	}

}
