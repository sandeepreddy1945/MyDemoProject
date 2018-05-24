/**
 * 
 */
package com.app.charts.main;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.app.chart.fx.FilesUtil;
import com.app.chart.model.RunJsonBoundary;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Sandeep Reddy Battula
 *
 */
public class ChartGroupUITester extends Application {

	public static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	public static double MIN_X = visualBounds.getMinX();
	public static double MIN_Y = visualBounds.getMinY();
	public static double MAX_X = visualBounds.getMaxX();
	public static double MAX_Y = visualBounds.getMaxY();
	public static double WIDTH = visualBounds.getWidth();
	public static double HEIGHT = visualBounds.getHeight();

	@Override
	public void start(Stage stage) throws Exception {

		stage.setMaximized(true);

		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = FileUtils.readFileToString(new File(FilesUtil.RUN_PROPS_PATH), Charset.defaultCharset());
		if (jsonStr != null && jsonStr.length() > 0) {
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			List<RunJsonBoundary> dataList = mapper.readValue(jsonStr,
					mapper.getTypeFactory().constructCollectionType(List.class, RunJsonBoundary.class));

			Scene scene = new Scene(new ChartGroupView(dataList, null), WIDTH, HEIGHT);
			stage.setScene(scene);
			stage.show();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}

}
