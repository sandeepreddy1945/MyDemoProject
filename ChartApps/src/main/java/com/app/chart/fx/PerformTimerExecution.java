/**
 * 
 */
package com.app.chart.fx;

import javafx.event.ActionEvent;

/**
 * @author Sandeep Reddy Battula
 *
 */
@FunctionalInterface
public interface PerformTimerExecution {

	public void executeTask(ActionEvent e);
}
