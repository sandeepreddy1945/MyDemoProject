/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.app.chart.model.TeamMember;

import eu.hansolo.tilesfx.Tile;
import javafx.animation.AnimationTimer;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Pagination;

/**
 * @author Sandeep
 *
 */
public class DashboardStackedBarChart extends DashboardAbstract {

	protected static final String MONTHLY_PERFOMANCE = "Monthly Perfomance";
	protected CategoryAxis categoryAxis;
	protected NumberAxis numberAxis;
	protected StackedBarChart<String, Number> barChart = null;
	protected String categoryAxisName;
	protected String numberAxisName;
	protected String title;

	protected Pagination pagination;
	protected static final int ITEMS_PER_PAGE = 5;
	protected int pageCount;
	protected int animationPageIndex = 0;
	private long lastTimerCall;
	public int resetAnimateCount = 1;
	private int page;
	private int size;

	public DashboardStackedBarChart(List<TeamMember> teamMembers) {
		this(teamMembers, MONTHLY_PERFOMANCE);
	}

	public DashboardStackedBarChart(List<TeamMember> teamMembers, String categoryAxisName, String numberAxisName) {
		this(teamMembers, categoryAxisName, numberAxisName, MONTHLY_PERFOMANCE);
	}

	public DashboardStackedBarChart(List<TeamMember> teamMembers, String title) {
		this(teamMembers, "Month Wise Perfomance", "Scores Achieved", title);
	}

	public DashboardStackedBarChart(List<TeamMember> teamMembers, String categoryAxisName, String numberAxisName,
			String title) {
		super(teamMembers);
		this.categoryAxisName = categoryAxisName;
		this.numberAxisName = numberAxisName;
		this.title = title;

		// animate the stack bar chart animation 
		animateGraph();
	}

	@Override
	public void startTimer(long now) {
		if (animationPageIndex <= pageCount) {
			pagination.setCurrentPageIndex(animationPageIndex++);
		} else {
			animationPageIndex = 0;
			pagination.setCurrentPageIndex(animationPageIndex);
		}

	}

	@SuppressWarnings("unchecked")
	public Tile createPage(int pageIndex) {

		page = pageIndex * ITEMS_PER_PAGE;
		size = pageIndex * ITEMS_PER_PAGE + ITEMS_PER_PAGE > teamMembers.size() ? teamMembers.size()
				: pageIndex * ITEMS_PER_PAGE + ITEMS_PER_PAGE;
		categoryAxis = new CategoryAxis();

		// reset the animate count
		resetAnimateCount++;
		/*
		 * xAxis.setCategories(FXCollections.<String>observableArrayList(
		 * Arrays.asList(austria, brazil, france, italy, usa)));
		 */ // if required
		numberAxis = new NumberAxis();
		barChart = new StackedBarChart<String, Number>(categoryAxis, numberAxis);

		// 3 charts per page so initializng the 3 graphs data.
		XYChart.Series<String, Number> series1 = new Series<>();
		XYChart.Series<String, Number> series2 = new Series<>();
		XYChart.Series<String, Number> series3 = new Series<>();

		for (int i = page; i < size; i++) {
			String name = teamMembers.get(i).getName();
			// presentedSeriesData(series1, series2, series3, i, name);
		}

		categoryAxis.setLabel(categoryAxisName);
		numberAxis.setLabel(numberAxisName);
		barChart.setTitle(title);
		barChart.setCategoryGap(20);

		barChart.getData().addAll(series1, series2, series3);

		Tile leaderBoardTile = generateCustomTile(barChart, "Team Perfomance", 430, 500);

		return leaderBoardTile;
	}

	protected void presentedSeriesData(XYChart.Series<String, Number> series1, XYChart.Series<String, Number> series2,
			XYChart.Series<String, Number> series3, int i, String name) {
		series1.getData().add(new XYChart.Data<String, Number>(name, teamMembers.get(i).getScore1()));
		series2.getData().add(new XYChart.Data<String, Number>(name, teamMembers.get(i).getScore2()));
		series3.getData().add(new XYChart.Data<String, Number>(name, teamMembers.get(i).getScore3()));
	}

	@Override
	public void initUI() {
		// set the terms for pagination
		pagination = new Pagination();
		double pageSizes = (double) teamMembers.size() / (double) ITEMS_PER_PAGE;
		pageCount = new BigDecimal(pageSizes).setScale(0, RoundingMode.UP).intValue();
		pagination = new Pagination(pageCount, 0);
		pagination.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
		pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);

		// add the style sheets to the UI / currently no required
		// getStylesheets().add(STYLESHEET_PATH);

		// add the appgination to UI
		getChildren().add(generateCustomTile(pagination, "", 450, 500, ""));

		// add the black background.
		setBackground(DashboardUtil.blackBackGround());

	}

	private void animateGraph() {
		lastTimerCall = System.nanoTime();

		AnimationTimer animationTimer = new AnimationTimer() {

			private int count = resetAnimateCount;

			@Override
			public void handle(long now) {
				if (now > lastTimerCall + 4_500_000_000L && barChart != null && count < resetAnimateCount) {

					XYChart.Series<String, Number> series1 = barChart.getData().get(0);
					// series1.getData().clear();
					XYChart.Series<String, Number> series2 = barChart.getData().get(1);
					// series2.getData().clear();
					XYChart.Series<String, Number> series3 = barChart.getData().get(2);
					// series3.getData().clear();
					for (int i = page; i < size; i++) {
						String name = teamMembers.get(i).getName();
						System.out.println(teamMembers.get(i).getName());
						presentedSeriesData(series1, series2, series3, i, name);
					}
					count++;
				}

			}
		};

		animationTimer.start();
	}

}
