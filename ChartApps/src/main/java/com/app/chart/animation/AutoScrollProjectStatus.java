/**
 * 
 */
package com.app.chart.animation;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Marker;

import com.app.chart.fx.FilesUtil;
import com.app.chart.model.ProjectStatusBoundary;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sandeep
 *
 *         DOnot modify the values very hard to alogn again.
 */
@Slf4j
public class AutoScrollProjectStatus extends HBox {

	private double height;
	private SequentialTransition sequentialTransition = new SequentialTransition();
	private int place;
	private List<ProjectStatusBoundary> projectStatusBoundaries = new ArrayList<>();
	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * @param obs
	 * @param width
	 */
	public AutoScrollProjectStatus(double height) {
		super(10);
		this.height = height;

		// initialize boundary from file.
		loadListFromFile();

		initUI();
	}

	private void loadListFromFile() {
		if (new File(FilesUtil.DASHBOARD_PROJECT_STATUS_FILE).exists()) {
			try {
				String jsonData = FileUtils.readFileToString(new File(FilesUtil.DASHBOARD_PROJECT_STATUS_FILE),
						Charset.defaultCharset());
				mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
				projectStatusBoundaries = mapper.readValue(jsonData,
						mapper.getTypeFactory().constructCollectionType(List.class, ProjectStatusBoundary.class));

				Collections.reverse(projectStatusBoundaries);
				// tableView.fireEvent(null);
			} catch (IOException e) {
				log.error("loadListFromFile", e);
			}
		}

	}

	private void initUI() {
		place = 100;
		if (projectStatusBoundaries != null && projectStatusBoundaries.size() > 0) {
			VBox stackPane = new VBox(5);
			// add some gap to pane
			// stackPane.setRotate(270);
			// stackPane.setMaxHeight(150);

			projectStatusBoundaries.stream().forEach(s -> {
				Label label = new Label(s.getTeamName());
				stackPane.getChildren().add(label);
				label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
				label.setWrapText(s.getTeamName().length() > 14 ? Boolean.TRUE : Boolean.FALSE);
				label.setTextFill(Color.WHITE);
				label.setBackground(determineBackGround(s.getStatusColor()));
				label.setPadding(new Insets(label.isWrapText() ? 40 : 50, 30, label.isWrapText() ? 40 : 50, 30));
				label.setMinWidth(210);
				label.setRotate(270);
				TranslateTransition translateTransition = new TranslateTransition(Duration.millis(9999), label);
				translateTransition.setFromY(-height );
				translateTransition.setToY(place);
				place = place + 90;
				translateTransition.setCycleCount(1);
				translateTransition.setAutoReverse(false);
				translateTransition.play();
				translateTransition.setDelay(Duration.millis(999));
				sequentialTransition.getChildren().add(translateTransition);

			});

			sequentialTransition.setCycleCount(Timeline.INDEFINITE);
			sequentialTransition.setAutoReverse(true);
			sequentialTransition.play();
			stackPane.setAlignment(Pos.CENTER);

			StackPane stackPane2 = new StackPane(stackPane);
			// stackPane2.setAlignment(Pos.BASELINE_LEFT);

			setMinWidth(150);
			setMaxWidth(150);
			setAlignment(Pos.TOP_CENTER);
			getChildren().add(stackPane2);
		}

	}

	private Background determineBackGround(String name) {
		Color color = name.equalsIgnoreCase("GREEN") ? Color.GREEN
				: name.equalsIgnoreCase("ORANGE") ? Color.ORANGE
						: name.equalsIgnoreCase("RED") ? Color.RED : Color.GREEN;

		return new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));

	}

}
