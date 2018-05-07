/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.PerspectiveCamera;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * @author Sandeep
 *
 */
public class DashboardUI {

	private static final int NUMERIC_10 = 10;
	private static final int NUMERIC_5 = 5;

	public static double TILE_WIDTH = 300;
	public static double TILE_HEIGHT = 300;
	
	public static double IMG_TILE_WIDTH = 250;
	public static double IMG_TILE_HEIGHT = 300;

	private VBox verticalBox;
	private HBox box1;
	private HBox box2;
	private HBox box3;
	private HBox box4;
	private HBox box5;
	private HBox box6;

	/**
	 * 
	 */
	public DashboardUI() {
		initUI();
	}

	private void initUI() {
		// vertical pane
		verticalBox = new VBox(NUMERIC_10);

		// set the background to black fav mode.
		verticalBox.setBackground(DashboardUtil.blackBackGround());

		// set the alignment
		verticalBox.setAlignment(Pos.CENTER);

		// camera view prospective
		PerspectiveCamera camera = new PerspectiveCamera();
		camera.setFieldOfView(10);

		// TODO to set the camera to the stage

		// horizontal box elements for graphs.
		box1 = new HBox(NUMERIC_10);
		box2 = new HBox(NUMERIC_10);
		box3 = new HBox(NUMERIC_10);
		box4 = new HBox(NUMERIC_10);
		box5 = new HBox(NUMERIC_10);
		box6 = new HBox(NUMERIC_10);
	}

	private void buildHeaderPanel() {

	}

}
