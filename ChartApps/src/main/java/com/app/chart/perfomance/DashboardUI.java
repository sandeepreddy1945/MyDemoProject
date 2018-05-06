/**
 * 
 */
package com.app.chart.perfomance;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author Sandeep
 *
 */
public class DashboardUI {

	private static final int NUMERIC_10 = 10;
	private static final int NUMERIC_5 = 5;

	private VBox verticalBox;
	private HBox box1;
	private HBox box2;
	private HBox box3;
	private HBox box4;
	private HBox box5;
	private HBox box6;

	private ImageView imageView1;
	private ImageView imageView2;

	/**
	 * 
	 */
	public DashboardUI() {
		initUI();
	}

	private void initUI() {
		// vertical pane
		verticalBox = new VBox(NUMERIC_10);

		// horizontal box elements for graphs.
		box1 = new HBox(NUMERIC_10);
		box2 = new HBox(NUMERIC_10);
		box3 = new HBox(NUMERIC_10);
		box4 = new HBox(NUMERIC_10);
		box5 = new HBox(NUMERIC_10);
		box6 = new HBox(NUMERIC_10);

		// images for leader board.
		imageView1 = new ImageView();
		imageView2 = new ImageView();
		
	}

}
