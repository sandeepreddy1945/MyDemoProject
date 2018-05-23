/**
 * 
 */
package com.app.charts.main;

import com.app.chart.fx.DisplayImage;
import com.app.chart.fx.DisplayOrgChart;
import com.app.chart.fx.DisplayPerfomance;
import com.app.chart.fx.PerformTimerExecution;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Sandeep Reddy Battula
 *
 */
public class TimerUtils implements DisplayImage, DisplayOrgChart, DisplayPerfomance, PerformTimerExecution {

	/**
	 * A time Line task with a delay of 2 minutes for each slide.
	 */
	Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(2.0), this::executeTask));

	/**
	 * 
	 */
	public TimerUtils() {
		// start the timer in a indefinite state.
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	public void pauseTimer() {
		timeline.pause();
	}

	public void stopTimer() {
		timeline.stop();
	}

	public void playTimer() {
		// start the play if only its paused or stopped.
		if (timeline.getStatus() == Status.PAUSED || timeline.getStatus() == Status.STOPPED) {
			timeline.play();
		}
	}

	public void playFromStart() {
		timeline.playFromStart();
	}

	public Timeline getTimeLine() {
		return timeline;
	}

	@Override
	public void executeTask(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayPerfomanceBoard(String perfomanceData, Stage stage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayOrgChart(String data, Stage stage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayImage(String imageData, Stage stage) {
		// TODO Auto-generated method stub

	}

}
