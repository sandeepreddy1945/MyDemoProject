/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.app.chart.model.TeamMember;

import eu.hansolo.tilesfx.Tile;
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

	private static final String MONTHLY_PERFOMANCE = "Monthly Perfomance";
	private final List<TeamMember> teamMembers;
	private CategoryAxis categoryAxis;
	private NumberAxis numberAxis;
	private StackedBarChart<String, Number> barChart = null;
	private final String categoryAxisName;
	private final String numberAxisName;
	private final String title;

	private Pagination pagination;
	private static final int ITEMS_PER_PAGE = 5;
	private int pageCount;
	private int animationPageIndex = 0;

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
		this.teamMembers = teamMembers;
		this.categoryAxisName = categoryAxisName;
		this.numberAxisName = numberAxisName;
		this.title = title;
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

		int page = pageIndex * ITEMS_PER_PAGE;
		int size = pageIndex * ITEMS_PER_PAGE + ITEMS_PER_PAGE > teamMembers.size() ? teamMembers.size()
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
			series1.getData().add(new XYChart.Data<String, Number>(name, teamMembers.get(i).getScore1()));
			series2.getData().add(new XYChart.Data<String, Number>(name, teamMembers.get(i).getScore2()));
			series3.getData().add(new XYChart.Data<String, Number>(name, teamMembers.get(i).getScore3()));
		}

		categoryAxis.setLabel(categoryAxisName);
		numberAxis.setLabel(numberAxisName);
		barChart.setTitle(title);
		barChart.setCategoryGap(20);

		barChart.getData().addAll(series1, series2, series3);

		Tile leaderBoardTile = generateCustomTile(barChart);

		return leaderBoardTile;
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
		getChildren().add(pagination);

		// add the black background.
		setBackground(DashboardUtil.blackBackGround());

	}

}
