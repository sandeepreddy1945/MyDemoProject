/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.app.chart.model.TeamMember;

import javafx.animation.AnimationTimer;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Pagination;
import javafx.scene.layout.StackPane;

/**
 * @author Sandeep
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
	private int page;
	private int size;
	private AnimationTimer animationTimer;

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
	public StackPane createPage(int pageIndex) {

		page = pageIndex * ITEMS_PER_PAGE;
		size = pageIndex * ITEMS_PER_PAGE + ITEMS_PER_PAGE > teamMembers.size() ? teamMembers.size()
				: pageIndex * ITEMS_PER_PAGE + ITEMS_PER_PAGE;
		categoryAxis = new CategoryAxis();

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
			presentedSeriesData(series1, series2, series3, i, name);
		}

		categoryAxis.setLabel(categoryAxisName);
		numberAxis.setLabel(numberAxisName);
		barChart.setTitle(title);
		barChart.setCategoryGap(20);

		// set the names for the series of the intrevals
		series1.setName(teamMembers.get(0).getIntreval1());
		series2.setName(teamMembers.get(0).getIntreval2());
		series3.setName(teamMembers.get(0).getIntreval3());

		barChart.getData().addAll(series1, series2, series3);

		// stop and start the animation timer once the page is reloaded.
		// This consevers the threads as well.
		// Make sure this is added after the bar charrt is set with data of series.
		if (animationTimer != null) {
			lastTimerCall = System.nanoTime();
			animationTimer.start();
		}

		// Tile leaderBoardTile = generateCustomTile(barChart, "Team Perfomance", 430,
		// 500);
		StackPane pane = new StackPane(barChart);
		pane.setPrefSize(430, 470);

		return pane;
	}

	/**
	 * Set the initial values to 0 ..And in the later stage animate it to actual
	 * values . <br>
	 * 
	 * @See Java bug on bar chart . Need to do this as there is a java bug outhere
	 *      causing disturbance in displaying labels .<br>
	 *      So first set the label names and then set the values to them during
	 *      animation.
	 * @param series1
	 * @param series2
	 * @param series3
	 * @param i
	 * @param name
	 */
	protected void presentedSeriesData(XYChart.Series<String, Number> series1, XYChart.Series<String, Number> series2,
			XYChart.Series<String, Number> series3, int i, String name) {

		series1.getData().add(new XYChart.Data<String, Number>(name.substring(0, name.indexOf(" ")), 0));
		series2.getData().add(new XYChart.Data<String, Number>(name.substring(0, name.indexOf(" ")), 0));
		series3.getData().add(new XYChart.Data<String, Number>(name.substring(0, name.indexOf(" ")), 0));
	}

	/**
	 * Set the initial values to 0 ..And in the later stage animate it to actual
	 * values . <br>
	 * 
	 * @See Java bug on bar chart . Need to do this as there is a java bug outhere
	 *      causing disturbance in displaying labels .<br>
	 *      So first set the label names and then set the values to them during
	 *      animation.
	 * @param series1
	 * @param series2
	 * @param series3
	 * @param i
	 * @param name
	 * @param count
	 */
	protected void presentedSeriesData(XYChart.Series<String, Number> series1, XYChart.Series<String, Number> series2,
			XYChart.Series<String, Number> series3, int i, String name, int count) {

		if (series1.getData().get(count) != null && series2.getData().get(count) != null
				&& series3.getData().get(count) != null) {
			series1.getData().get(count).setYValue(teamMembers.get(i).getValueAdd());
			series2.getData().get(count).setYValue(teamMembers.get(i).getQuality());
			series3.getData().get(count).setYValue(teamMembers.get(i).getOnTime());
		}

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
		getChildren().add(generateCustomTile(pagination, "Employee- Quarterly Index Trend", 450, 500, "Individual Stats"));

		// add the black background.
		setBackground(DashboardUtil.blackBackGround());

	}

	private void animateGraph() {
		lastTimerCall = System.nanoTime();

		animationTimer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				if (now > lastTimerCall + 1_500_000_000L && barChart != null) {
					int count = 0;
					XYChart.Series<String, Number> series1 = barChart.getData().get(0);
					XYChart.Series<String, Number> series2 = barChart.getData().get(1);
					XYChart.Series<String, Number> series3 = barChart.getData().get(2);
					for (int i = page; i < size; i++) {
						String name = teamMembers.get(i).getName();
						presentedSeriesData(series1, series2, series3, i, name, count++);
					}
					count++;
					animationTimer.stop();
				}

			}
		};

		animationTimer.start();
	}
	
	/**
	 * Retrieves the Animation Timer to Stop it in the Main App . Critical Fix
	 * required to fix the Timer issues running in the background.
	 * 
	 * @return
	 */
	public AnimationTimer fetchAnimationTimer() {
		return animationTimer;
	}

}
