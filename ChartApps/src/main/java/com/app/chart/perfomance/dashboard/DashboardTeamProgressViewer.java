/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.NeedleShape;
import eu.hansolo.medusa.Gauge.NeedleSize;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.TickMarkType;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * @author Sandeep
 *
 */
public class DashboardTeamProgressViewer extends HBox {

	private MultiGauge gauge;
	private long lastTimerCall;
	double mainPercentage;
	double currentProgress;
	double pendingProgress;
	private AnimationTimer animationTimer;

	/**
	 * 
	 */
	public DashboardTeamProgressViewer(double mainPercentage, double currentProgress, double pendingProgress) {
		super(5);
		gauge = new MultiGauge();

		StackPane pane = new StackPane(gauge);

		Tile tile = TileBuilder.create().skinType(eu.hansolo.tilesfx.Tile.SkinType.CUSTOM).prefSize(300, 250)
				.title("Team Performance").
				// TODO think of a name for this text .
				text("").graphic(pane).roundedCorners(true).build();

		// pane.setPadding(new Insets(20));
		// pane.setBackground(DashboardUtil.BLACK_BACKGROUND);

		getChildren().add(tile);

		this.mainPercentage = mainPercentage;
		this.currentProgress = currentProgress;
		this.pendingProgress = pendingProgress;

		// add values.
		Platform.runLater(() -> {
			gauge.getMainGauge().setValue(0);
			gauge.getCurrentGauge().setValue(0);
			gauge.getPendingGauge().setValue(0);
		});

		animateValues();
	}

	private void animateValues() {
		lastTimerCall = System.nanoTime();
		animationTimer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				if (now > lastTimerCall + 3_500_000_000L) {
					gauge.getMainGauge().setValue(mainPercentage);
					gauge.getCurrentGauge().setValue(currentProgress);
					gauge.getPendingGauge().setValue(pendingProgress);

					// stop the timer single attribute here
					animationTimer.stop();
				}
			}
		};

		animationTimer.start();
	}

	/**
	 * Retrieves the Animation Timer to Stop it in the Main App . Critical Fix
	 * required to fix the Timer issues running in the background.
	 * 
	 * In this class already a call is done to stop the timer. So need not stop it
	 * again explictly. JUzz for safety check here.
	 * 
	 * @return
	 */
	public AnimationTimer fetchAnimationTimer() {
		return animationTimer;
	}

	public class MultiGauge extends Region {
		private static final double PREFERRED_WIDTH = 320;
		private static final double PREFERRED_HEIGHT = 250;
		private static final double MINIMUM_WIDTH = 5;
		private static final double MINIMUM_HEIGHT = 5;
		private static final double MAXIMUM_WIDTH = 1024;
		private static final double MAXIMUM_HEIGHT = 1024;
		private double size;
		private Gauge rpmGauge;
		private Gauge currentProgress;
		private Gauge pendingProgress;
		private Pane pane;

		// ******************** Constructors **************************************
		public MultiGauge() {
			init();
			initGraphics();
			registerListeners();
		}

		// ******************** Initialization ************************************
		private void init() {
			if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0
					|| Double.compare(getWidth(), 0.0) <= 0 || Double.compare(getHeight(), 0.0) <= 0) {
				if (getPrefWidth() > 0 && getPrefHeight() > 0) {
					setPrefSize(getPrefWidth(), getPrefHeight());
				} else {
					setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
				}
			}

			if (Double.compare(getMinWidth(), 0.0) <= 0 || Double.compare(getMinHeight(), 0.0) <= 0) {
				setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
			}

			if (Double.compare(getMaxWidth(), 0.0) <= 0 || Double.compare(getMaxHeight(), 0.0) <= 0) {
				setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHT);
			}
		}

		private void initGraphics() {
			rpmGauge = GaugeBuilder.create().borderPaint(Color.WHITE).foregroundBaseColor(Color.WHITE)
					.prefSize(400, 250).startAngle(290).angleRange(220).minValue(0).maxValue(1000).valueVisible(true)
					.minorTickMarksVisible(false).majorTickMarkType(TickMarkType.BOX)
					.mediumTickMarkType(TickMarkType.BOX).title("Performance").needleShape(NeedleShape.ROUND)
					.needleSize(NeedleSize.THICK).needleColor(Color.rgb(234, 67, 38)).knobColor(Gauge.DARK_COLOR)
					.customTickLabelsEnabled(true).customTickLabelFontSize(40)
					.customTickLabels("0", "", "200", "", "400", "", "600", "", "800", "", "1000").animated(true)
					.build();

			currentProgress = GaugeBuilder.create().skinType(SkinType.HORIZONTAL).prefSize(170, 170)
					.foregroundBaseColor(Color.WHITE).title("CURRENT").valueVisible(false).angleRange(90).maxValue(600)
					.needleShape(NeedleShape.ROUND).needleSize(NeedleSize.THICK).needleColor(Color.rgb(234, 67, 38))
					.minorTickMarksVisible(false).mediumTickMarksVisible(false).majorTickMarkType(TickMarkType.BOX)
					.knobColor(Gauge.DARK_COLOR).customTickLabelsEnabled(true).customTickLabelFontSize(36)
					.customTickLabels("0", "", "", "", "", "400", "", "", "", "", "800").animated(true).build();

			pendingProgress = GaugeBuilder.create().skinType(SkinType.HORIZONTAL).prefSize(170, 170).valueVisible(true)
					.foregroundBaseColor(Color.WHITE).title("PENDING").valueVisible(false).angleRange(90)
					.needleShape(NeedleShape.ROUND).needleSize(NeedleSize.THICK).needleColor(Color.rgb(234, 67, 38))
					.minorTickMarksVisible(false).mediumTickMarksVisible(false).majorTickMarkType(TickMarkType.BOX)
					.knobColor(Gauge.DARK_COLOR).customTickLabelsEnabled(true).customTickLabelFontSize(36)
					.customTickLabels("0", "", "", "", "", "50", "", "", "", "", "100").animated(true).build();
			pane = new Pane(currentProgress, pendingProgress, rpmGauge);

			getChildren().setAll(pane);
		}

		private void registerListeners() {
			widthProperty().addListener(observable -> resize());
			heightProperty().addListener(observable -> resize());
		}

		// ******************** Methods *******************************************
		public Gauge getMainGauge() {
			return rpmGauge;
		}

		public Gauge getCurrentGauge() {
			return currentProgress;
		}

		public Gauge getPendingGauge() {
			return pendingProgress;
		}

		// ******************** Resizing ******************************************
		private void resize() {
			double width = getWidth() - getInsets().getLeft() - getInsets().getRight();
			double height = getHeight() - getInsets().getTop() - getInsets().getBottom();
			size = width < height ? width : height;

			if (size > 0) {
				pane.setMaxSize(size, size);
				pane.relocate((getWidth() - size) * 0.5, (getHeight() - size) * 0.5);

				rpmGauge.setPrefSize(size, size);

				currentProgress.setPrefSize(size * 0.425, size * 0.425);
				currentProgress.relocate(size * 0.1, size * 0.5625);

				pendingProgress.setPrefSize(size * 0.425, size * 0.425);
				pendingProgress.relocate(size * 0.475, size * 0.5625);
			}
		}
	}
}

// current progress commented code
/*
 * GaugeBuilder.create().skinType(SkinType.HORIZONTAL).prefSize(170,
 * 170).autoScale(false)
 * .foregroundBaseColor(Color.WHITE).title("CURRENT").valueVisible(false).
 * angleRange(90).minValue(100)
 * .maxValue(500).needleShape(NeedleShape.ROUND).needleSize(NeedleSize.THICK)
 * .needleColor(Color.rgb(234, 67,
 * 38)).minorTickMarksVisible(false).mediumTickMarksVisible(false)
 * .majorTickMarkType(TickMarkType.BOX).knobColor(Gauge.DARK_COLOR).
 * customTickLabelsEnabled(true)
 * .customTickLabelFontSize(36).customTickLabels("0", "", "", "", "", "200", "",
 * "", "", "", "500") .animated(true).build()
 */
