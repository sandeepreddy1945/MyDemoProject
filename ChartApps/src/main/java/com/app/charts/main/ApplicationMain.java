/**
 * 
 */
package com.app.charts.main;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.app.chart.cache.ChartCacheManager;
import com.app.chart.fx.ChartWebEngine;
import com.app.chart.fx.FilesUtil;
import com.app.chart.image.display.DisplayImage;
import com.app.chart.model.PerfomanceBoardBoundary;
import com.app.chart.model.RunJsonBoundary;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.app.chart.perfomance.dashboard.ui.DashboardUI;
import com.app.chart.run.ui.DisplayBoardConstants;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.ehcache.Element;

/**
 * @author Sandeep Reddy Battula
 *
 *         <b> Main Class File The Application Runs on .</b> <br>
 *         Important that this class should be left untouched . Touching this
 *         class methods would break the application or effect its
 *         performance.<br>
 */
public class ApplicationMain extends Application {

	public static final String PERFOMANCEBOARDLIST = "PERFOMANCEBOARDLIST";
	public static final String RUNJSONCACHE = "RUNJSONCACHE";
	/**
	 * Visual Bounds of the Screen.
	 */
	public static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	public static double MIN_X = visualBounds.getMinX();
	public static double MIN_Y = visualBounds.getMinY();
	public static double MAX_X = visualBounds.getMaxX();
	public static double MAX_Y = visualBounds.getMaxY();
	public static double WIDTH = visualBounds.getWidth();
	public static double HEIGHT = visualBounds.getHeight();

	/**
	 * A time Line task with a delay of 2 minutes for each slide.
	 */
	Timeline timeline = null;

	// Fetch the Run JSON Boundary to fetch the sequence of running and headers.

	private ObjectMapper mapper = new ObjectMapper();
	ChartCacheManager chartCacheManager = null;
	List<RunJsonBoundary> runJsonBoundaries = new ArrayList<>();
	private Stage stage;

	private int pageCount = -1;
	private List<PerfomanceBoardBoundary> perfomanceBoardBoundaries = new ArrayList<>();
	private int dashBoardCount = -1;
	private Timeline dashBoardTimeLine;

	private boolean isDashBoardRunning = false;
	private boolean isNormalOnesRunning = false;

	// TODO Some how push everything to cache so that app can be made more clean.

	@Override
	public void init() throws Exception {
		// initialize the cache
		// this initilizes the cache manager and puts a cache entry.
		chartCacheManager = ChartCacheManager.getInstance();

		// fetch the runjson list in order to evaluate the order of running the items
		// and then cache it.
		buildRunJsonListFromFile();
		chartCacheManager.getMpsChartCache().put(new Element(RUNJSONCACHE, this.runJsonBoundaries));

		// read the dashboardlist and put it to cache.
		buildPerfomanceBoardBoundary();
	}

	/**
	 * Builds the perfomance board boundary required for showing the performance
	 * details.
	 * 
	 * @throws IOException
	 */
	private void buildPerfomanceBoardBoundary() throws IOException {
		String jsonStr = FileUtils.readFileToString(new File(FilesUtil.DASHBOARD_CONTENT_DATA),
				Charset.defaultCharset());
		if (jsonStr != null && jsonStr.length() > 0) {
			perfomanceBoardBoundaries = mapper.readValue(jsonStr,
					mapper.getTypeFactory().constructCollectionType(List.class, PerfomanceBoardBoundary.class));
			chartCacheManager.getMpsChartCache().put(new Element(PERFOMANCEBOARDLIST, perfomanceBoardBoundaries));
		}
	}

	/**
	 * Builds the Run Json Boundaries from scratch.
	 * 
	 * @param runJsonFile
	 * @throws IOException
	 */
	private void buildRunJsonListFromFile() throws IOException {

		String jsonStr = FileUtils.readFileToString(new File(FilesUtil.RUN_PROPS_PATH), Charset.defaultCharset());
		if (jsonStr != null && jsonStr.length() > 0) {
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			List<RunJsonBoundary> dataList = mapper.readValue(jsonStr,
					mapper.getTypeFactory().constructCollectionType(List.class, RunJsonBoundary.class));
			runJsonBoundaries = dataList;
		}
	}

	/**
	 * A big JDK Bug https://bugs.openjdk.java.net/browse/JDK-8089209 which makes
	 * the screen to exit out of full screen when new scene is set. AA big mistake
	 * which cannot be covered.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		stage.setMaximized(true);
		stage.setFullScreen(true);
		stage.setMinWidth(WIDTH);
		stage.setMinHeight(HEIGHT);
		// stage.centerOnScreen();
		stage.setTitle("MPS Organizational View");
		stage.setFullScreenExitHint("");
		stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.isControlDown() && e.getCode().ordinal() == KeyCode.F.ordinal()) {
				if (!stage.isFullScreen()) {
					// just make it to full screen when it is not .in other case juzz leave it as
					// is.
					stage.setFullScreen(true);
				}
			}
		});

		// juzz display a blank page at the start.
		Scene scene = new Scene(new HBox(), WIDTH, HEIGHT);
		// start the timer once the UI is initiated.
		timeline = new Timeline(new KeyFrame(Duration.millis(9999), this::executeTask));
		stage.setScene(scene);
		stage.show();
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

	}

	@Override
	public void stop() throws Exception {
		// shut down the cache manager once the application is closed.
		chartCacheManager.getCacheManager().shutdown();
	}

	public void executeTask(ActionEvent e) {
		// set the flag is Normal running to true when ever its called,
		isNormalOnesRunning = true;

		RunJsonBoundary r = fetchNextValueFromList();
		if (DisplayBoardConstants.chart.name().equals(r.getType())) {
			String fileName = r.getPath();
			String filePath = FilesUtil.MAIN_APP_PATH + FilesUtil.SLASH + fileName + FilesUtil.SLASH + "index.html";
			String headerTxt = r.getDisplayTxt();
			// by default header is applicable for a org chart.
			// initialize the chart web engine
			ChartWebEngine chartWebEngine = new ChartWebEngine().initialize();
			// set the primary stage object to webview for popup displays
			chartWebEngine.setParenStage(stage);
			HBox box = new HBox();
			VBox mainBox = new VBox(5);
			mainBox.getChildren().add(DashboardUtil.HeaderSegment(box, headerTxt));
			mainBox.getChildren().add(chartWebEngine.getWebView());
			HBox footerSegment = DashboardUtil.FooterSegment();
			footerSegment.setPrefHeight(25);
			mainBox.getChildren().add(footerSegment);
			box.getChildren().add(mainBox);

			Scene scene = new Scene(box, WIDTH, HEIGHT);
			stage.setScene(scene);
			stage.setFullScreen(true);

			URL url = null;
			try {
				url = new File(filePath).toURI().toURL();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// start putting the chart now
			chartWebEngine.displayData(url.toExternalForm());

			stage.toFront();

		} else if (DisplayBoardConstants.dashboard.name().equals(r.getType())) {
			// as it has a single file we need to stop the timer here and start a new one
			// for this.
			timeline.pause();// pause the main timer.
			// as it dashboard run put it to false
			isNormalOnesRunning = false;
			isDashBoardRunning = true;
			dashBoardTimeLine = new Timeline(new KeyFrame(Duration.millis(9999), event -> {
				try {
					executeDashboardTask(event);
				} catch (Exception e1) {
					// If failed don't block load the next one
					e1.printStackTrace();
					// executeTask(e);
				}
			}));

			dashBoardTimeLine.setCycleCount(Animation.INDEFINITE);
			dashBoardTimeLine.play();
		} else if (DisplayBoardConstants.image.name().equals(r.getType())) {
			HBox box = new DisplayImage(r.getPath(), r.isHeaderApplicable(), r.getDisplayTxt());
			Scene scene = new Scene(box, WIDTH, HEIGHT);
			stage.setScene(scene);
			stage.toFront();
			stage.setFullScreen(true);
		} else if (DisplayBoardConstants.customer.name().equals(r.getType())) {
			// not yet implemented.
		}

	}

	public void executeDashboardTask(ActionEvent e) throws Exception {
		if (dashBoardCount == perfomanceBoardBoundaries.size()) {
			dashBoardTimeLine.stop();
			timeline.play();
			dashBoardCount = 0;// reset the count .back again.
			// as last one is reached put the flags back
			isNormalOnesRunning = true;
			isDashBoardRunning = false;
		} else {
			HBox box = new DashboardUI(fetchNextValueFromPerfomanceList()).dashBoardMainBox();
			Scene scene = new Scene(box, WIDTH, HEIGHT);
			stage.setScene(scene);
			stage.toFront();
			stage.setFullScreen(true);
		}
	}

	private PerfomanceBoardBoundary fetchNextValueFromPerfomanceList() {
		if (dashBoardCount == perfomanceBoardBoundaries.size() - 1) {
			// PerfomanceBoardBoundary pbb = perfomanceBoardBoundaries.get(dashBoardCount);
			dashBoardCount++;
			return null; // as max count is reached.
		} else {
			dashBoardCount++;
			return perfomanceBoardBoundaries.get(dashBoardCount);
		}
	}

	public RunJsonBoundary fetchNextValueFromList() {
		// if max count is reached re-initialize it back again
		if (pageCount == runJsonBoundaries.size() - 1) {
			pageCount = 0;
			return runJsonBoundaries.get(pageCount);
		} else {
			pageCount++;
			return runJsonBoundaries.get(pageCount);

		}

	}

	/**
	 * Loads the Next Page
	 * 
	 * @throws Exception
	 */
	public void loadNext() throws Exception {
		if (isNormalOnesRunning) {
			executeTask(null);
		} else if (isDashBoardRunning) {
			executeDashboardTask(null);
		}
	}

	/**
	 * Loads the previous page.
	 */
	public void loadPrevious() {
		if (isNormalOnesRunning) {
			if (pageCount <= 1) {
				pageCount = -1;
			} else {
				pageCount = pageCount - 2;
			}

		} else if (isDashBoardRunning) {
			if (dashBoardCount <= 1) {
				dashBoardCount = -1;
			} else {
				dashBoardCount = dashBoardCount - 2;
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
