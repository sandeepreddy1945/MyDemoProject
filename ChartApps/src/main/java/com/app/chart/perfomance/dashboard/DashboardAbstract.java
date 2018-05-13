/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.io.File;
import java.util.Collections;
import java.util.List;

import com.app.chart.model.TeamMember;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.skins.BarChartItem;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * @author Sandeep Reddy Battula
 *
 */
public abstract class DashboardAbstract extends HBox {

	private AnimationTimer animationTimer;
	private long lastTimerCall;
	protected final List<TeamMember> teamMembers;
	private File[] files;

	/**
	 * Default Constructor.
	 */
	public DashboardAbstract(List<TeamMember> teamMembers) {
		this(teamMembers, null);
	}

	/**
	 * 
	 * @param teamMembers
	 * @param files
	 */
	public DashboardAbstract(List<TeamMember> teamMembers, File... files) {
		super(5);
		this.teamMembers = teamMembers;
		this.files = files;
		initUI();
		initTimer();
	}

	/**
	 * Common method available for all the UI Components.
	 */
	public abstract void initUI();

	/**
	 * Method to be used for the timer .Implemented in dashboard utilities.
	 * 
	 * @param now
	 */
	public abstract void startTimer(long now);

	public void initTimer() {
		lastTimerCall = System.nanoTime();
		// run as a thread safe one.
		Platform.runLater(() -> {
			animationTimer = new AnimationTimer() {

				@Override
				public void handle(long now) {
					if (now > lastTimerCall + 3_500_000_000L) {
						// call the animation timer here in for all the instances applicable.
						startTimer(now);
						lastTimerCall = now;
					}
				}
			};
		});

	}

	public void startTimer() {
		animationTimer.start();
	}

	public void stopTimer() {
		animationTimer.stop();
	}

	public void sortTeamMembers() {
		Collections.sort(teamMembers, DashboardUtil.TeamMemberSorter.getInstance());
	}

	public void setDimensions(final int width, final int height) {
		setMinSize(width, height);
	}

	public void setDefaultDimensions() {
		setMinSize(300, 300);
	}

	public Color getRandomTileColor() {
		return Color.color(Math.random(), Math.random(), Math.random());
	}

	/**
	 * 
	 * @param leaderboardChartItems
	 * @return
	 */
	public Tile generateBarChartTile(List<BarChartItem> leaderboardChartItems) {
		Tile leaderBoardTile = generateBarChartTile(leaderboardChartItems.stream().toArray(BarChartItem[]::new));

		return leaderBoardTile;
	}

	/**
	 * 
	 * @param leaderboardChartItems
	 * @return
	 */
	public Tile generateBarChartTile(BarChartItem... leaderboardChartItems) {
		Tile leaderBoardTile = generateBarChartTile(DashboardUtil.TILE_WIDTH, DashboardUtil.TILE_HEIGHT,
				leaderboardChartItems);
		return leaderBoardTile;
	}

	/**
	 * 
	 * @param width
	 * @param height
	 * @param leaderboardChartItems
	 * @return
	 */
	public Tile generateBarChartTile(double width, double height, BarChartItem... leaderboardChartItems) {
		Tile leaderBoardTile = generateBarChartTile(width, height, "LeaderBoard", leaderboardChartItems);
		return leaderBoardTile;
	}

	/**
	 * 
	 * @param width
	 * @param height
	 * @param title
	 * @param leaderboardChartItems
	 * @return
	 */
	public Tile generateBarChartTile(double width, double height, String title, BarChartItem... leaderboardChartItems) {
		Tile leaderBoardTile = generateBarChartTile(width, height, title, "LeaderBoard", leaderboardChartItems);
		return leaderBoardTile;
	}

	/**
	 * 
	 * @param width
	 * @param height
	 * @param title
	 * @param btmText
	 * @param leaderboardChartItems
	 * @return
	 */
	public Tile generateBarChartTile(double width, double height, String title, String btmText,
			BarChartItem... leaderboardChartItems) {
		Tile leaderBoardTile = TileBuilder.create().skinType(SkinType.BAR_CHART).prefSize(width, height).title(title).
		// TODO to decide a text for the leader board display And decided to put team
		// name here.
				text(btmText).barChartItems(leaderboardChartItems).decimals(0).build();
		return leaderBoardTile;
	}

	/**
	 * 
	 * @param chart
	 * @return
	 */
	public Tile generateCustomTile(javafx.scene.chart.Chart chart) {
		Tile tile = generateCustomTile(chart, "Team Rating Chart");
		return tile;
	}

	/**
	 * 
	 * @param chart
	 * @param title
	 * @return
	 */
	public Tile generateCustomTile(javafx.scene.chart.Chart chart, String title) {
		Tile tile = generateCustomTile(chart, title, DashboardUtil.TILE_WIDTH, DashboardUtil.TILE_HEIGHT);
		return tile;
	}

	/**
	 * 
	 * @param chart
	 * @param title
	 * @param width
	 * @param height
	 * @return
	 */
	public Tile generateCustomTile(javafx.scene.chart.Chart chart, String title, double width, double height) {
		Tile tile = generateCustomTile(chart, title, width, height, "Team Perfomance");
		return tile;
	}

	/**
	 * 
	 * @param chart
	 * @param title
	 * @param width
	 * @param height
	 * @param btmText
	 * @return
	 */
	public Tile generateCustomTile(javafx.scene.chart.Chart chart, String title, double width, double height,
			String btmText) {
		Tile tile = TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(width, height).title(title).
		// TODO think of a name for this text .
				text(btmText).graphic(chart).roundedCorners(true).build();
		return tile;
	}
}