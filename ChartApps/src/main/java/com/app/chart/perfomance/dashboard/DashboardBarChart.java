/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.app.chart.model.TeamMember;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Pagination;
import javafx.scene.layout.StackPane;

/**
 * @author Sandeep
 *
 */
public class DashboardBarChart extends DashboardAbstract {

	private static final String MONTHLY_PERFOMANCE = "Monthly Perfomance";
	private CategoryAxis categoryAxis;
	private NumberAxis numberAxis;
	private BarChart<String, Number> barChart = null;
	private final String categoryAxisName;
	private final String numberAxisName;
	private final String title;

	private Pagination pagination;
	private static final int ITEMS_PER_PAGE = 3;
	private int pageCount;
	private int animationPageIndex = 0;
	private int page;
	private int size;
	private long lastTimerCall;
	private AnimationTimer animationTimer;

	public DashboardBarChart(List<TeamMember> teamMembers) {
		this(teamMembers, MONTHLY_PERFOMANCE);
	}

	public DashboardBarChart(List<TeamMember> teamMembers, String categoryAxisName, String numberAxisName) {
		this(teamMembers, categoryAxisName, numberAxisName, MONTHLY_PERFOMANCE);
	}

	public DashboardBarChart(List<TeamMember> teamMembers, String title) {
		this(teamMembers, "Month Wise Perfomance", "Scores Achieved", title);
	}

	public DashboardBarChart(List<TeamMember> teamMembers, String categoryAxisName, String numberAxisName,
			String title) {
		super(teamMembers);
		this.categoryAxisName = categoryAxisName;
		this.numberAxisName = numberAxisName;
		this.title = title;

		// animate the graph
		Platform.runLater(() -> animateGraph());
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
		numberAxis = new NumberAxis(0, 100, 50);
		barChart = new BarChart<String, Number>(categoryAxis, numberAxis);

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
		barChart.setBarGap(3);
		barChart.setCategoryGap(20);

		barChart.getData().addAll(series1, series2, series3);

		// set the names for the series of the intrevals
		series1.setName(teamMembers.get(0).getIntreval1());
		series2.setName(teamMembers.get(0).getIntreval2());
		series3.setName(teamMembers.get(0).getIntreval3());

		// stop and start the animation timer once the page is reloaded.
		// This consevers the threads as well.
		if (animationTimer != null) {
			lastTimerCall = System.nanoTime();
			animationTimer.start();
		}

		StackPane pane = new StackPane(barChart);
		pane.setPrefSize(430, 470);
		// Tile leaderBoardTile = generateCustomTile(barChart, "Team Perfomance", 430,
		// 500);

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

		series1.getData().add(new XYChart.Data<String, Number>(name, 0));
		series2.getData().add(new XYChart.Data<String, Number>(name, 0));
		series3.getData().add(new XYChart.Data<String, Number>(name, 0));
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
			series1.getData().get(count).setYValue(teamMembers.get(i).getScore1());
			series2.getData().get(count).setYValue(teamMembers.get(i).getScore2());
			series3.getData().get(count).setYValue(teamMembers.get(i).getScore3());
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

		// add the appgination to UI using Tile
		// TODO change the naming here if wanted
		getChildren().add(generateCustomTile(pagination, "Monthly Trend Analysis", 450, 500, "Stats"));

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
						System.out.println(teamMembers.get(i).getName());
						presentedSeriesData(series1, series2, series3, i, name, count++);
					}

					animationTimer.stop();

				}

			}
		};

		animationTimer.start();
	}
}
