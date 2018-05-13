/**
 * 
 */
package com.app.chart.perfomance.dashboard.ui;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.app.chart.model.TeamMember;
import com.app.chart.perfomance.dashboard.DashboardBarChart;
import com.app.chart.perfomance.dashboard.DashboardHeader;
import com.app.chart.perfomance.dashboard.DashboardImageViewer;
import com.app.chart.perfomance.dashboard.DashboardPieChart;
import com.app.chart.perfomance.dashboard.DashboardTeamMemberScoreViewer;
import com.app.chart.perfomance.dashboard.DashboardTeamProgressViewer;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.app.chart.perfomance.dashboard.sidebar.DashboardSidePane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
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
		vbox.setPrefSize(WIDTH, HEIGHT);
		vbox.setMinSize(WIDTH - 160, HEIGHT);

		HBox secondLayer = initializeTopImages();

		HBox thirdLayer = initializeLeaderBoard();

		// Third Layer Charts
		HBox barChart = initializeBarChart();
		HBox pieChart = initializePieChart();
		HBox progressViewer = initializeTeamProgressViewer();

		secondLayer.getChildren().add(0, progressViewer);

		thirdLayer.getChildren().addAll(barChart, pieChart);

		vbox.getChildren().addAll(headerBox, secondLayer, thirdLayer);

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
			dashboardHeader.setMinSize(WIDTH - 160, 100);
			dashboardHeader.setPrefSize(WIDTH, 100);

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
	 * &nbsp;&nbsp; <img alt="Leader Board" src="teamperfomance.png"/>
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
	 * &nbsp;&nbsp; <img alt="Leader Board" src="piechart.png"/>
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
	 * &nbsp;&nbsp; <img alt="Leader Board" src="leaderboard.png"/>
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

	private HBox initializeTeamProgressViewer() {
		DashboardTeamProgressViewer progressViewer = new DashboardTeamProgressViewer(2400, 1210, 90);
		return progressViewer;
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

		// stage.setMaximized(true);
		stage.setMinHeight(HEIGHT);
		stage.setMinWidth(WIDTH);
		// stage.setFullScreen(true);

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
