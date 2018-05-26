/**
 * 
 */
package com.app.chart.animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * @author Sandeep
 *
 */
public class AutoScrollData extends HBox {

	private ObservableList<String> obs = FXCollections.observableArrayList();
	private double width;
	private int lblCount = 0;

	/**
	 * @param obs
	 * @param width
	 */
	public AutoScrollData(ObservableList<String> obs, double width) {
		super(10);
		this.obs = obs;
		this.width = width;

		initUI();
	}

	private void initUI() {
		if (obs != null && obs.size() > 0) {
			StackPane stackPane = new StackPane();
			Label lbl = new Label(obs.get(0));
			lbl.setFont(Font.font("Arial", FontWeight.BOLD, 16));
			lbl.setTextFill(Color.BLACK);

			stackPane.getChildren().add(0, lbl);
			KeyValue value1 = new KeyValue(stackPane.translateXProperty(), 0);
			KeyValue value2 = new KeyValue(stackPane.translateXProperty(), width);

			KeyFrame keyFrame = new KeyFrame(Duration.millis(5555), e -> {
				stackPane.getChildren().remove(0);
				stackPane.getChildren().add(0, fecthNextLbl());
			}, value1, value2);

			Timeline timeline = new Timeline(keyFrame);
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.setAutoReverse(false);
			timeline.play();

			getChildren().add(stackPane);
		}

	}

	private Label fecthNextLbl() {
		if (lblCount == obs.size() - 1) {
			Label lbl = new Label(obs.get(lblCount));
			lbl.setFont(Font.font("Arial", FontWeight.BOLD, 16));
			lbl.setTextFill(Color.BLACK);
			lblCount = -1;

			return lbl;
		} else {
			lblCount++;
			Label lbl = new Label(obs.get(lblCount));
			lbl.setFont(Font.font("Arial", FontWeight.BOLD, 16));
			lbl.setTextFill(Color.BLACK);
			return lbl;

		}
	}

}
