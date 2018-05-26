/**
 * 
 */
package com.app.chart.animation;

import com.app.chart.perfomance.dashboard.DashboardUtil;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 * @author Sandeep
 *
 */
public class AutoScrollData1 extends HBox {

	private ObservableList<String> obs = FXCollections.observableArrayList();
	private double width;
	private SequentialTransition sequentialTransition = new SequentialTransition();
	private int place;

	/**
	 * @param obs
	 * @param width
	 */
	public AutoScrollData1(ObservableList<String> obs, double width) {
		super(10);
		this.obs = obs;
		this.width = width;

		initUI();
	}

	private void initUI() {
		place = -1;
		if (obs != null) {
			StackPane stackPane = new StackPane();

			obs.stream().forEach(s -> {
				Label label = new Label(s);
				stackPane.getChildren().add(label);
				label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
				label.setTextFill(Color.WHITE);
				label.setBackground(DashboardUtil.blackBackGround());
				label.setPadding(new Insets(40));
				TranslateTransition translateTransition = new TranslateTransition(Duration.millis(9999), label);
				translateTransition.setFromX(Screen.getPrimary().getVisualBounds().getWidth() + 200);
				translateTransition.setToX(place);
				place = place + 200;
				translateTransition.setCycleCount(1);
				translateTransition.setAutoReverse(false);
				translateTransition.play();
				sequentialTransition.getChildren().add(translateTransition);

			});

			sequentialTransition.setCycleCount(Timeline.INDEFINITE);
			sequentialTransition.setAutoReverse(true);
			sequentialTransition.play();

			getChildren().add(stackPane);
		}

	}

}
