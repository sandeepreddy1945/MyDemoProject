/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.layout.HBox;

/**
 * @author Sandeep Reddy Battula
 *
 */
public abstract class DashboardAbstract extends HBox {

	private AnimationTimer animationTimer;

	/**
	 * Default Constructor.
	 */
	public DashboardAbstract() {

	}

	/**
	 * Method to be used for the timer .Implemented in dashboard utilities.
	 * 
	 * @param now
	 */
	public abstract void startTimer(long now);

	/**
	 * Common method available for all the UI Components.
	 */
	public abstract void initUI();

	public void initTimer() {

		// run as a thread safe one.
		Platform.runLater(() -> {
			animationTimer = new AnimationTimer() {

				@Override
				public void handle(long now) {
					// call the animation timer here in for all the instances applicable.
					startTimer(now);
				}
			};
		});

	}

	public void startTimer() {
		animationTimer.start();
	}

	public void stopTimer() {
		animationTimer.stop();
	}

}
