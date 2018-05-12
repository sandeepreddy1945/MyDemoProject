/**
 * 
 */
package com.app.chart.perfomance.dashboard.ui;

import java.io.File;
import java.net.URL;
import java.util.List;

import com.app.chart.model.TeamMember;
import com.app.chart.perfomance.dashboard.DashboardHeader;
import com.app.chart.perfomance.dashboard.sidebar.DashboardSidePane;
import com.jfoenix.controls.JFXDrawer;

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
	private VBox box;

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

		box = new VBox(10);

		// 1st Header
		// initializeHeader();

		box.getChildren().addAll(initializeHeader());
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
			dashboardHeader.setMinSize(WIDTH, 200);

			// initalize the side panel
			DashboardSidePane dashboardSidePane = new DashboardSidePane(dashboardHeader.getImageView1());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dashboardHeader;
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
		stage.setAlwaysOnTop(true);

		Scene scene = new Scene(box, WIDTH, HEIGHT);

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
