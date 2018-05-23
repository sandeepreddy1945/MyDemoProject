/**
 * 
 */
package com.app.charts.main;

import com.fasterxml.jackson.databind.ObjectMapper;

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

	// Fetch the Run JSON Boundary to fetch the sequence of running and headers.

	private ObjectMapper objectMapper = new ObjectMapper();
	

	// TODO Some how push everything to cache so that app can be made more clean.

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
