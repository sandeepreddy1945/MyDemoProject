/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import javafx.animation.AnimationTimer;

/**
 * @author Sandeep Reddy Battula
 *
 */
public class AbstractTimer extends AnimationTimer {

	private volatile boolean running;

	@Override
	public void handle(long now) {

	}

	@Override
	public void start() {
		super.start();
		running = true;
	}

	@Override
	public void stop() {
		super.stop();
		running = false;
	}

	public boolean isRunning() {
		return running;
	}

}
