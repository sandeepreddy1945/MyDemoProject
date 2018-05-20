/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile.SkinType;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * @author Sandeep
 *
 */
public class DashboardTeamBarChart extends HBox {

	private final Number totalProgress;
	private final Number currentProgress;
	private final Number pendingProgress;
	private AnimationTimer animationTimer;
	private long lastTimerCall;
	private BarChart<Number, String> bc;

	/**
	 * 
	 */
	public DashboardTeamBarChart(double totalProgress, double currentProgress, double pendingProgress) {
		super(5);
		this.totalProgress = totalProgress;
		this.currentProgress = currentProgress;
		this.pendingProgress = pendingProgress;

		initUI();

		// animate the data
		animateData();
	}

	private void initUI() {
		final NumberAxis xAxis = new NumberAxis();
		final CategoryAxis yAxis = new CategoryAxis();
		bc = new BarChart<Number, String>(xAxis, yAxis);
		bc.setTitle("Current Sprint Activities");
		xAxis.setLabel("Points Achieved");
		xAxis.setTickLabelRotation(90);
		yAxis.setLabel("In Progress");

		bc.setBarGap(1);
		bc.setCategoryGap(0);

		XYChart.Series<Number, String> series1 = new Series<>();
		XYChart.Series<Number, String> series2 = new Series<>();
		XYChart.Series<Number, String> series3 = new Series<>();
		series1.setName("Total Points");
		series2.setName("Commited Points");
		series3.setName("Pending Points");
		// currently set the data to 0 and later animate it using animater,
		series3.getData().add(new XYChart.Data<Number, String>(0, "Total Points"));
		series2.getData().add(new XYChart.Data<Number, String>(0, "Commited Points"));
		series1.getData().add(new XYChart.Data<Number, String>(0, "Pending Points"));

		bc.getData().addAll(series1, series2, series3);

		StackPane pane = new StackPane(bc);
		pane.setMinSize(730, 190);
		pane.setPrefSize(730, 190);

		getChildren().add(generateCustomTile(pane, "", 765, 210, ""));

		setBackground(DashboardUtil.blackBackGround());

	}

	private Tile generateCustomTile(Node node, String title, double width, double height, String btmText) {
		Tile tile = TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(width, height).title(title).
		// TODO think of a name for this text .
				text(btmText).graphic(node).roundedCorners(true).build();
		return tile;
	}

	private void animateData() {
		lastTimerCall = System.nanoTime();
		animationTimer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				if (now > lastTimerCall + 2_500_000_000L && bc != null) {
					XYChart.Series<Number, String> series1 = bc.getData().get(0);
					XYChart.Series<Number, String> series2 = bc.getData().get(1);
					XYChart.Series<Number, String> series3 = bc.getData().get(2);

					series3.getData().get(0).setXValue(totalProgress);
					series2.getData().get(0).setXValue(currentProgress);
					series1.getData().get(0).setXValue(pendingProgress);

					animationTimer.stop();
				}

			}
		};
		animationTimer.start();
	}
}
