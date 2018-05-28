/**
 * 
 */
package com.app.chart.perfomance.dashboard.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.slf4j.Marker;

import com.app.chart.animation.AutoScrollProjectStatus;
import com.app.chart.animation.ScrollTextDataUI.ScrollTxtTableBoundary;
import com.app.chart.fx.FilesUtil;
import com.app.chart.model.CurrentSprintBoundary;
import com.app.chart.model.ManagerDetailBoundary;
import com.app.chart.model.PerfomanceBoardBoundary;
import com.app.chart.model.PerfomanceMeterBoundary;
import com.app.chart.model.ScrollTexts;
import com.app.chart.model.SunburstBoundary;
import com.app.chart.model.TeamMember;
import com.app.chart.perfomance.dashboard.DashboardBarChart;
import com.app.chart.perfomance.dashboard.DashboardHeader;
import com.app.chart.perfomance.dashboard.DashboardImageViewer;
import com.app.chart.perfomance.dashboard.DashboardIndividualStatsViewer;
import com.app.chart.perfomance.dashboard.DashboardPieChart;
import com.app.chart.perfomance.dashboard.DashboardStackedBarChart;
import com.app.chart.perfomance.dashboard.DashboardSunburnChart;
import com.app.chart.perfomance.dashboard.DashboardTeamBarChart;
import com.app.chart.perfomance.dashboard.DashboardTeamMemberScoreViewer;
import com.app.chart.perfomance.dashboard.DashboardTeamProgressViewer;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.app.chart.perfomance.dashboard.sidebar.DashboardSidePane;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sandeep<br>
 *         Class with All sort of functonalities to dsiplay the Dashboard .<br>
 *         <b>Preview : </b> <br>
 *         <img alt="Dashboard" src="dashboard.png">
 *
 */
@Slf4j
public class DashboardUI extends Application {

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

	private final List<TeamMember> teamMembers;
	private VBox vbox;
	private DashboardSidePane dashboardSidePane;
	private HBox hbox;
	private final String headerTxt;
	private final ManagerDetailBoundary managerDetailBoundary;
	static ManagerDetailBoundary testBoundary = new ManagerDetailBoundary();
	private final SunburstBoundary sunburstBoundary;
	private DashboardSunburnChart sunburnChart;
	private final PerfomanceMeterBoundary perfomanceMeterBoundary;
	private final CurrentSprintBoundary currentSprintBoundary;

	/**
	 * @param teamMembers
	 * @throws Exception
	 */
	public DashboardUI(List<TeamMember> teamMembers, String headerTxt, ManagerDetailBoundary managerDetailBoundary)
			throws Exception {
		Collections.sort(teamMembers, DashboardUtil.TeamMemberSorter.getInstance());
		this.teamMembers = teamMembers;
		this.headerTxt = headerTxt;
		this.managerDetailBoundary = managerDetailBoundary;
		this.sunburstBoundary = null;
		this.perfomanceMeterBoundary = null;
		this.currentSprintBoundary = null;
		// init the UI using the init method.
		init();
	}

	/**
	 * This is constructor used for preview of the board.
	 * 
	 * @param teamMembers
	 * @param headerTxt
	 * @param managerDetailBoundary
	 * @param dialog
	 * @throws Exception
	 */
	public DashboardUI(List<TeamMember> teamMembers, String headerTxt, ManagerDetailBoundary managerDetailBoundary,
			SunburstBoundary sunburstBoundary, PerfomanceMeterBoundary perfomanceMeterBoundary,
			CurrentSprintBoundary currentSprintBoundary, Dialog dialog) throws Exception {
		// compare the elements using the sort available.
		Collections.sort(teamMembers, DashboardUtil.TeamMemberSorter.getInstance());
		// sort it by descending order now.
		Collections.reverse(teamMembers);
		this.teamMembers = teamMembers;
		this.headerTxt = headerTxt;
		this.managerDetailBoundary = managerDetailBoundary;
		this.sunburstBoundary = sunburstBoundary;
		this.perfomanceMeterBoundary = perfomanceMeterBoundary;
		this.currentSprintBoundary = currentSprintBoundary;
		// init the UI using the init method.
		init();

		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		// set onclose request for the dialog.
		Window window = dialog.getDialogPane().getScene().getWindow();
		window.setOnCloseRequest(event -> window.hide());

		// stage.setMaximized(true);
		stage.setMinHeight(HEIGHT);
		stage.setMinWidth(WIDTH);
		// stage.setFullScreen(true);

		Scene scene = new Scene(hbox, WIDTH - 10, HEIGHT - 10);
		hbox.setBackground(DashboardUtil.BLACK_BACKGROUND);

		stage.setScene(scene);

		Platform.runLater(() -> stage.show());

	}

	// The Actual Constructor that's used by the main App Program
	/**
	 * This is constructor used for preview of the board.
	 * 
	 * @param teamMembers
	 * @param headerTxt
	 * @param managerDetailBoundary
	 * @param sunburstBoundary
	 * @param perfomanceMeterBoundary
	 * @param currentSprintBoundary
	 * @throws Exception
	 */
	public DashboardUI(List<TeamMember> teamMembers, String headerTxt, ManagerDetailBoundary managerDetailBoundary,
			SunburstBoundary sunburstBoundary, PerfomanceMeterBoundary perfomanceMeterBoundary,
			CurrentSprintBoundary currentSprintBoundary) throws Exception {
		// compare the elements using the sort available.
		Collections.sort(teamMembers, DashboardUtil.TeamMemberSorter.getInstance());
		// sort it by descending order now.
		Collections.reverse(teamMembers);
		this.teamMembers = teamMembers;
		this.headerTxt = headerTxt;
		this.managerDetailBoundary = managerDetailBoundary;
		this.sunburstBoundary = sunburstBoundary;
		this.perfomanceMeterBoundary = perfomanceMeterBoundary;
		this.currentSprintBoundary = currentSprintBoundary;
		// init the UI using the init method.
		init();
		hbox.setBackground(DashboardUtil.BLACK_BACKGROUND);
		hbox.setMinSize(WIDTH, HEIGHT);

	}

	/**
	 * Alternate Boundary to the main boundary.
	 * 
	 * @param boardBoundary
	 * @throws Exception
	 */
	public DashboardUI(PerfomanceBoardBoundary boardBoundary) throws Exception {
		this(boardBoundary.getTeamMembers(), boardBoundary.getHeaderTxt(), boardBoundary.getManagerDetailBoundary(),
				boardBoundary.getSunburstBoundary(), boardBoundary.getPerfomanceMeterBoundary(),
				boardBoundary.getCurrentSprintBoundary());
	}

	/**
	 * Method used by the main app to render the UI with panel.
	 * 
	 * @return
	 */
	public HBox dashBoardMainBox() {
		return hbox;
	}

	/**
	 * Default Constructor.
	 * 
	 * @throws Exception
	 */
	public DashboardUI() throws Exception {
		// some default title juzz for testing
		this(teamMembers(), "Sandeep Reddy", testBoundary);
		testBoundary.setName("Sandeep Reddy");
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
		// Either Use the Bar chart or the sunbrun chart for display
		HBox sunbutnChart = initializeSunburnChart();

		HBox pieChart = initializePieChart();
		HBox progressViewer = initializeTeamProgressViewer();
		HBox stackBarChart = initializeStackBarChart();

		secondLayer.getChildren().add(0, progressViewer);
		secondLayer.getChildren().addAll(secondLayerChild, secondLayerChild2);

		// Either of The barchart or the stack can be used by uncommenting
		// TODO to decide on which chart to use.
		// display on sunburst when dispayable //else display barchart instead of it.
		if (sunburnChart.isSunburstChartDisplayable()) {
			thirdLayer.getChildren().addAll(/* barChart */ sunbutnChart, pieChart, stackBarChart);
		} else {
			thirdLayer.getChildren().addAll(barChart /* sunbutnChart */, pieChart, stackBarChart);
		}

		HBox fourthLayer = initializeIndividualStatsViewer();

		// set the insets for the layers
		secondLayer.setPadding(new Insets(0, 0, 0, 5));
		thirdLayer.setPadding(new Insets(0, 0, 0, 5));
		fourthLayer.setPadding(new Insets(0, 0, 0, 15));

		// footer for the dashboard
		HBox foorterTile = initializeFooter();

		// vbox.getChildren().addAll(headerBox, secondLayer, thirdLayer, fourthLayer,
		// foorterTile);

		// add the first header
		VBox headerCombp = new VBox(10);
		headerCombp.getChildren().add(headerBox);
		headerBox.setMaxWidth(WIDTH);
		VBox pBox = new VBox(10);
		pBox.setMinWidth(WIDTH - 140);
		pBox.getChildren().addAll(secondLayer, thirdLayer, fourthLayer);
		HBox phBox = new HBox(5);

		// add the project status
		VBox projectStatus = initializeProjectStatus();
		phBox.getChildren().addAll(pBox, projectStatus);
		headerCombp.getChildren().addAll(phBox, translationTextBox());

		vbox.getChildren().addAll(headerCombp, foorterTile);

		// dynamically add the drawer pane implicitly
		hbox.getChildren().addAll(vbox/* , projectStatus */);
	}

	/**
	 * Header Initialization.
	 */
	private HBox initializeHeader() {

		DashboardHeader dashboardHeader = null;
		try {
			InputStream f1 = DashboardUtil.class.getClassLoader()
					.getResourceAsStream("com/app/chart/images/nttlogo.png");
			InputStream f2 = DashboardUtil.class.getClassLoader()
					.getResourceAsStream("com/app/chart/images/ntt-data.png");

			dashboardHeader = new DashboardHeader(f1, f2, headerTxt);
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
			URL url1 = new File(FilesUtil.IMAGES_DIR_PATH + FilesUtil.SLASH + teamMembers.get(0).getPortalId() + ".png")
					.toURI().toURL();
			URL url2 = new File(FilesUtil.IMAGES_DIR_PATH + FilesUtil.SLASH + teamMembers.get(1).getPortalId() + ".png")
					.toURI().toURL();
			File logo1 = new File(url1.toURI().getPath());
			File logo2 = new File(url2.toURI().getPath());

			dashboardImageViewer = new DashboardImageViewer(logo1, logo2, teamMembers);

		} catch (Exception ex) {
			ex.printStackTrace();
			// when file not found
			URL url3 = getClass().getClassLoader().getResource("com/app/chart/images/default.png");

			File logo1;
			try {
				logo1 = new File(url3.toURI().getPath());
				File logo2 = new File(url3.toURI().getPath());

				dashboardImageViewer = new DashboardImageViewer(logo1, logo2, teamMembers);
			} catch (URISyntaxException e) {
				log.error( "initializeTopImages", e);
			} catch (IOException e) {
				log.error( "initializeTopImages", e);
			}

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
		/*
		 * List<TeamMember> list = teamMembers(); Collections.sort(list,
		 * DashboardUtil.TeamMemberSorter.getInstance()); Collections.reverse(list);
		 */
		DashboardTeamMemberScoreViewer scoreViewer = new DashboardTeamMemberScoreViewer(/* list */teamMembers);
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
		/*
		 * List<TeamMember> list = teamMembers(); Collections.sort(list,
		 * DashboardUtil.TeamMemberSorter.getInstance()); Collections.reverse(list);
		 */
		DashboardPieChart pieChart = new DashboardPieChart(/* list */teamMembers);
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
		/*
		 * List<TeamMember> list = teamMembers(); Collections.sort(list,
		 * DashboardUtil.TeamMemberSorter.getInstance()); Collections.reverse(list);
		 */
		DashboardBarChart barChart = new DashboardBarChart(/* list */ teamMembers);
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
		DashboardTeamProgressViewer progressViewer = null;
		if (perfomanceMeterBoundary != null) {
			progressViewer = new DashboardTeamProgressViewer(perfomanceMeterBoundary.getTotalPoints(),
					perfomanceMeterBoundary.getCurrentPoints(), perfomanceMeterBoundary.getBacklogPoints());
		} else {
			// by default set the values to 0
			progressViewer = new DashboardTeamProgressViewer(0, 0, 0);
		}
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
		/*
		 * List<TeamMember> list = teamMembers(); Collections.sort(list,
		 * DashboardUtil.TeamMemberSorter.getInstance()); Collections.reverse(list);
		 */
		DashboardStackedBarChart stackedBarChart = new DashboardStackedBarChart(/* list */teamMembers);
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
		/*
		 * List<TeamMember> list = teamMembers(); Collections.sort(list,
		 * DashboardUtil.TeamMemberSorter.getInstance()); Collections.reverse(list);
		 */
		DashboardIndividualStatsViewer statsViewer = new DashboardIndividualStatsViewer(/* list */teamMembers);
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
		DashboardTeamBarChart dashboardTeamBarChart = null;
		if (currentSprintBoundary != null) {
			dashboardTeamBarChart = new DashboardTeamBarChart(currentSprintBoundary.getTotalSprintPoints(),
					currentSprintBoundary.getCurrentSprintPoints(), currentSprintBoundary.getBacklogSprintPoints());
		} else {
			dashboardTeamBarChart = new DashboardTeamBarChart(0, 0, 0);
		}

		return dashboardTeamBarChart;
	}

	/**
	 * Initializes the Team Sunburn Chart of Releases <br>
	 * <b> Preview : <b> <br>
	 * &nbsp;&nbsp; <img alt="SunBurn Chart" src="sunburnchart.png">
	 * 
	 * @return
	 */
	private HBox initializeSunburnChart() {
		sunburnChart = new DashboardSunburnChart(sunburstBoundary);
		return sunburnChart;
	}

	private Tile initializeManagerPicture() {

		File logo1;
		Image image = null;
		try {
			URL url1 = new File(
					FilesUtil.IMAGES_DIR_PATH + FilesUtil.SLASH + managerDetailBoundary.getPortalId() + ".png").toURI()
							.toURL();
			logo1 = new File(url1.toURI().getPath());
			image = new Image(FileUtils.openInputStream(logo1));
		} catch (Exception e) {
			log.error( "initializeManagerPicture", e);
			image = new Image(getClass().getClassLoader().getResourceAsStream("com/app/chart/images/default.png"));
		}

		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(250);
		imageView.setFitWidth(200);
		imageView.setPreserveRatio(false);

		Tile tile = TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(250, 250).title("Team Manager")
				.text(this.managerDetailBoundary.getName()).graphic(imageView).roundedCorners(true).build();

		return tile;
	}

	private VBox initializeProjectStatus() {
		// currently not working.
		HBox mainBox = new AutoScrollProjectStatus(HEIGHT - 150);
		VBox box = new VBox(5);
		Text text = new Text("Project Status");
		text.setFill(Color.WHITE);
		text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		text.setTextAlignment(TextAlignment.LEFT);
		HBox m = new HBox();
		m.setAlignment(Pos.TOP_LEFT);
		box.getChildren().add(text);
		box.getChildren().addAll(m, mainBox);
		box.setAlignment(Pos.TOP_LEFT);

		return box;

	}

	private HBox initializeFooter() {
		Text creditsText = new Text("© Sandeep Reddy Battula");
		creditsText.setTextAlignment(TextAlignment.RIGHT);
		creditsText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		creditsText.setFill(Paint.valueOf("#ffffff"));

		Text copyRights = new Text("Copy Rights Reserved.");
		copyRights.setTextAlignment(TextAlignment.LEFT);
		copyRights.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		copyRights.setFill(Paint.valueOf("#ffffff"));

		HBox copyRightsBox = new HBox(copyRights);
		copyRightsBox.setPadding(new Insets(0, 15, 0, 0));
		copyRightsBox.setMinSize(WIDTH / 2 - 20, 10);
		copyRightsBox.setBackground(DashboardUtil.BLACK_BACKGROUND);
		copyRightsBox.setAlignment(Pos.BOTTOM_LEFT);

		HBox creditsBox = new HBox(creditsText);
		creditsBox.setPadding(new Insets(0, 15, 0, 0));
		creditsBox.setMinSize(WIDTH / 2 - 20, 10);
		creditsBox.setBackground(DashboardUtil.BLACK_BACKGROUND);
		creditsBox.setAlignment(Pos.BOTTOM_RIGHT);
		HBox.setHgrow(copyRightsBox, Priority.ALWAYS);

		HBox footerBox = new HBox(copyRightsBox, creditsBox);
		footerBox.setAlignment(Pos.BOTTOM_CENTER);
		footerBox.setMinSize(WIDTH - 10, 10);

		return footerBox;

	}

	private HBox translationTextBox() {

		String buildCongratsTxt = "Congrats !!! " + teamMembers.get(0).getName() + " and "
				+ teamMembers.get(1).getName() + " for the Best Performance.";

		Label label = new Label(buildCongratsTxt);
		label.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
		label.setTextFill(Color.BLACK);

		TranslateTransition t = new TranslateTransition(Duration.seconds(30), label);
		t.setFromX(WIDTH);
		t.setToX(-WIDTH);
		t.setAutoReverse(false);
		HBox box = new HBox(label);
		Background background = new Background(
				new BackgroundFill(Color.web("#ffbf80"), CornerRadii.EMPTY, Insets.EMPTY));
		box.setBackground(background);
		SequentialTransition st = new SequentialTransition();
		st.getChildren().addAll(t);
		// fecth the scroll texts.
		List<ScrollTexts> scrollTexts = new ArrayList<>();
		if (new File(FilesUtil.DASHBOARD_PROJECT_SCROLL_TEXT_FILE).exists()) {
			try {
				String jsonData = FileUtils.readFileToString(new File(FilesUtil.DASHBOARD_PROJECT_SCROLL_TEXT_FILE),
						Charset.defaultCharset());
				final ObjectMapper mapper = new ObjectMapper();
				mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
				scrollTexts = mapper.readValue(jsonData,
						mapper.getTypeFactory().constructCollectionType(List.class, ScrollTexts.class));
				// tableView.fireEvent(null);
			} catch (IOException e) {
				log.error( "loadListFromFile", e);
			}
		}

		// add them to the list
		if (scrollTexts != null && scrollTexts.size() > 0) {
			scrollTexts.stream().forEach(s -> {
				Label label1 = new Label(s.getScrollText());
				label1.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
				label1.setTextFill(Color.BLACK);

				TranslateTransition t1 = new TranslateTransition(Duration.seconds(25), label1);
				t1.setFromX(WIDTH);
				t1.setToX(-WIDTH);
				t1.setAutoReverse(false);
				box.getChildren().add(label1);
				st.getChildren().add(t1);
			});
		}
		st.setCycleCount(Timeline.INDEFINITE);
		st.play();
		return box;
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
		member.setPortalId(String.valueOf(randomNumber()));
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

		Scene scene = new Scene(hbox, WIDTH - 10, HEIGHT - 10);
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
