/**
 * 
 */
package com.app.chart.perfomance.dashboard.ui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.app.chart.model.TeamMember;
import com.app.chart.perfomance.dashboard.DashboardBarChart;
import com.app.chart.perfomance.dashboard.DashboardHeader;
import com.app.chart.perfomance.dashboard.DashboardImageViewer;
import com.app.chart.perfomance.dashboard.DashboardIndividualStatsViewer;
import com.app.chart.perfomance.dashboard.DashboardPieChart;
import com.app.chart.perfomance.dashboard.DashboardStackedBarChart;
import com.app.chart.perfomance.dashboard.DashboardTeamBarChart;
import com.app.chart.perfomance.dashboard.DashboardTeamMemberScoreViewer;
import com.app.chart.perfomance.dashboard.DashboardTeamProgressViewer;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.app.chart.perfomance.dashboard.sidebar.DashboardSidePane;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile.SkinType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Sandeep
 *
 */
public class DashboardUI extends Application {

	/**
	 * Visual Bounds of the Screen.
	 */
	static Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
	static double MIN_X = visualBounds.getMinX();
	static double MIN_Y = visualBounds.getMinY();
	static double MAX_X = visualBounds.getMaxX();
	static double MAX_Y = visualBounds.getMaxY();
	static double WIDTH = visualBounds.getWidth();
	static double HEIGHT = visualBounds.getHeight();

	private final List<TeamMember> teamMembers;
	private VBox vbox;
	private DashboardSidePane dashboardSidePane;
	private HBox hbox;

	/**
	 * @param teamMembers
	 */
	public DashboardUI(List<TeamMember> teamMembers) {
		this.teamMembers = teamMembers;
	}

	/**
	 * Default Constructor.
	 */
	public DashboardUI() {
		this.teamMembers = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#init()
	 */
	@Override
	public void init() throws Exception {

		hbox = new HBox(5);
		vbox = new VBox(5);

		// 1st Header
		// initializeHeader();

		HBox headerBox = initializeHeader();
		headerBox.setPadding(new Insets(0, 0, 5, 0));

		vbox.setPrefSize(WIDTH, HEIGHT);
		vbox.setMinSize(WIDTH - 160, HEIGHT);

		HBox secondLayer = initializeTopImages();
		HBox secondLayerChild = initializeHeaderTeamPointsViewer();
		Tile secondLayerChild2 = initializeManagerPicture();
		secondLayer.setSpacing(5);

		HBox thirdLayer = initializeLeaderBoard();

		// Third Layer Charts
		HBox barChart = initializeBarChart();
		HBox pieChart = initializePieChart();
		HBox progressViewer = initializeTeamProgressViewer();
		HBox stackBarChart = initializeStackBarChart();

		secondLayer.getChildren().add(0, progressViewer);
		secondLayer.getChildren().addAll(secondLayerChild, secondLayerChild2);

		thirdLayer.getChildren().addAll(barChart, pieChart, stackBarChart);

		HBox fourthLayer = initializeIndividualStatsViewer();

		vbox.getChildren().addAll(headerBox, secondLayer, thirdLayer, fourthLayer);

		// dynamically add the drawer pane implicityly
		hbox.getChildren().addAll(vbox);
	}

	/**
	 * Header Initialization.
	 */
	private HBox initializeHeader() {

		DashboardHeader dashboardHeader = null;
		try {
			URL url1 = ClassLoader.getSystemResource("com/app/chart/images/nttlogo.png");
			URL url2 = ClassLoader.getSystemResource("com/app/chart/images/ntt-data.png");
			File logo1 = new File(url1.toURI().getPath());
			File logo2 = new File(url2.toURI().getPath());

			dashboardHeader = new DashboardHeader(logo1, logo2, "Sandeep Reddy Battula");
			dashboardHeader.setMinSize(WIDTH - 160, 90);
			dashboardHeader.setPrefSize(WIDTH, 90);

			// initalize the side panel by passing the main panel.
			// side pane explictly uses it hide and unhide the side pane.
			// TODO to check if there is any other alternative than this.
			dashboardSidePane = new DashboardSidePane(dashboardHeader.getImageView1(), hbox);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dashboardHeader;
	}

	private HBox initializeTopImages() {
		DashboardImageViewer dashboardImageViewer = null;
		try {
			// TODO to change this to dynamic
			// Kept Juzz for testing.
			URL url1 = ClassLoader.getSystemResource("com/app/chart/images/nttlogo.png");
			URL url2 = ClassLoader.getSystemResource("com/app/chart/images/ntt-data.png");
			File logo1 = new File(url1.toURI().getPath());
			File logo2 = new File(url1.toURI().getPath());

			dashboardImageViewer = new DashboardImageViewer(logo1, logo2, null);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dashboardImageViewer;

	}

	/**
	 * Initialize the Leader Board Chart<br>
	 * <b>Preview : <b><br>
	 * <br>
	 * &nbsp;&nbsp; <img alt="Leader Board" src="leaderboard.png"/>
	 * 
	 * @return
	 */
	private HBox initializeLeaderBoard() {
		List<TeamMember> list = teamMembers();
		Collections.sort(list, DashboardUtil.TeamMemberSorter.getInstance());
		Collections.reverse(list);
		DashboardTeamMemberScoreViewer scoreViewer = new DashboardTeamMemberScoreViewer(list);
		return scoreViewer;

	}

	/**
	 * Initialize the Pie Board Chart<br>
	 * <b>Preview : <b><br>
	 * <br>
	 * &nbsp;&nbsp; <img alt="Pie Chart" src="piechart.png"/>
	 * 
	 * @return
	 */
	private HBox initializePieChart() {
		List<TeamMember> list = teamMembers();
		Collections.sort(list, DashboardUtil.TeamMemberSorter.getInstance());
		Collections.reverse(list);
		DashboardPieChart pieChart = new DashboardPieChart(list);
		return pieChart;
	}

	/**
	 * Initialize the Perfomance Bar Chart<br>
	 * <b>Preview : <b><br>
	 * <br>
	 * &nbsp;&nbsp; <img alt="Team Perfomance" src="teamperfomance.png"/>
	 * 
	 * @return
	 */
	private HBox initializeBarChart() {
		List<TeamMember> list = teamMembers();
		Collections.sort(list, DashboardUtil.TeamMemberSorter.getInstance());
		Collections.reverse(list);
		DashboardBarChart barChart = new DashboardBarChart(list);
		return barChart;

	}

	/**
	 * Initialize the Perfomance Meter Viewer<br>
	 * <b>Preview : <b><br>
	 * <br>
	 * &nbsp;&nbsp; <img alt="Progress Viewer" src="progressViewer.png"/>
	 * 
	 * @return
	 */
	private HBox initializeTeamProgressViewer() {
		DashboardTeamProgressViewer progressViewer = new DashboardTeamProgressViewer(2400, 1210, 90);
		return progressViewer;
	}

	/**
	 * Initialize the Perfomance Meter Viewer<br>
	 * <b>Preview : <b><br>
	 * <br>
	 * &nbsp;&nbsp; <img alt="Stats Viewer" src="statsviewer.png"/>
	 * 
	 * @return
	 */
	private HBox initializeStackBarChart() {
		List<TeamMember> list = teamMembers();
		Collections.sort(list, DashboardUtil.TeamMemberSorter.getInstance());
		Collections.reverse(list);
		DashboardStackedBarChart stackedBarChart = new DashboardStackedBarChart(list);
		return stackedBarChart;
	}

	/**
	 * Initialize the Individual Stats Viewer<br>
	 * <b>Preview : <b><br>
	 * <br>
	 * &nbsp;&nbsp; <img alt="Stack Chart" src="stackbarchart.png"/>
	 * 
	 * @return
	 */
	private HBox initializeIndividualStatsViewer() {
		List<TeamMember> list = teamMembers();
		Collections.sort(list, DashboardUtil.TeamMemberSorter.getInstance());
		Collections.reverse(list);
		DashboardIndividualStatsViewer statsViewer = new DashboardIndividualStatsViewer(list);
		return statsViewer;
	}

	/**
	 * Initialize the Team Points Scored Viewer<br>
	 * <b>Preview : <b><br>
	 * <br>
	 * &nbsp;&nbsp; <img alt="Team Points Scored" src="teampointsscore.png"/>
	 * 
	 * @return
	 */
	private HBox initializeHeaderTeamPointsViewer() {
		DashboardTeamBarChart dashboardTeamBarChart = new DashboardTeamBarChart(100, 90, 10);
		return dashboardTeamBarChart;
	}

	private Tile initializeManagerPicture() {
		URL url1 = ClassLoader.getSystemResource("com/app/chart/images/nttlogo.png");
		File logo1;
		Image image = null;
		try {
			logo1 = new File(url1.toURI().getPath());
			image = new Image(FileUtils.openInputStream(logo1));
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(250);
		imageView.setFitWidth(200);
		imageView.setPreserveRatio(true);

		Tile tile = TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(250, 250).title("Custom Tile")
				.text("Whatever text").graphic(imageView).roundedCorners(true).build();

		return tile;
	}

	// TODO to remove this variables later
	DashboardTeamMemberScoreViewer teamMember;
	DashboardPieChart dashboardPieChart;
	static Random random = new Random(12000);

	// TODO to remove this later
	public static List<TeamMember> teamMembers() {
		return Arrays.asList(randomTemMember(), randomTemMember(), randomTemMember(), randomTemMember(),
				randomTemMember(), randomTemMember(), randomTemMember(), randomTemMember(), randomTemMember());

	}

	// TODO to remove this later
	private static TeamMember randomTemMember() {
		TeamMember member = new TeamMember();
		member.setDescription(randomString());
		member.setIntreval1(randomString());
		member.setIntreval2(randomString());
		member.setIntreval3(randomString());
		member.setLink(randomString());
		member.setPortalId(randomString());
		member.setScore1(randomNumber());
		member.setScore2(randomNumber());
		member.setScore3(randomNumber());
		member.setParent(randomString());
		member.setTeam(randomString());
		member.setName(randomString());
		member.setLink(randomString());
		member.setQuality(randomNumber());
		member.setOnTime(randomNumber());
		member.setValueAdd(randomNumber());

		return member;
	}

	private static int randomNumber() {
		return random.nextInt(99);
	}

	private static String randomString() {
		return String.valueOf("asdfg" + randomNumber());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#stop()
	 */
	@Override
	public void stop() throws Exception {
		super.stop();
	}

	@Override
	public void start(Stage stage) throws Exception {

		stage.setMaximized(true);
		stage.setMinHeight(HEIGHT);
		stage.setMinWidth(WIDTH);
		stage.setFullScreen(true);

		Scene scene = new Scene(hbox, WIDTH, HEIGHT);
		hbox.setBackground(DashboardUtil.BLACK_BACKGROUND);

		stage.setScene(scene);
		Platform.runLater(() -> stage.show());

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}

}
