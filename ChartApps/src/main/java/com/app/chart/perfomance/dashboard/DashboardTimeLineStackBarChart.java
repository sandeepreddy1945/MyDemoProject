package com.app.chart.perfomance.dashboard;

import java.util.List;

import com.app.chart.model.TeamMember;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

/**
 * 
 * @author Sandeep
 *
 */
public class DashboardTimeLineStackBarChart extends DashboardStackedBarChart {

	public DashboardTimeLineStackBarChart(List<TeamMember> teamMembers) {
		this(teamMembers, MONTHLY_PERFOMANCE);
	}

	public DashboardTimeLineStackBarChart(List<TeamMember> teamMembers, String categoryAxisName,
			String numberAxisName) {
		this(teamMembers, categoryAxisName, numberAxisName, MONTHLY_PERFOMANCE);
	}

	public DashboardTimeLineStackBarChart(List<TeamMember> teamMembers, String title) {
		this(teamMembers, "Month Wise Performance", "Scores Achieved", title);
	}

	public DashboardTimeLineStackBarChart(List<TeamMember> teamMembers, String categoryAxisName, String numberAxisName,
			String title) {
		super(teamMembers);
		this.categoryAxisName = categoryAxisName;
		this.numberAxisName = numberAxisName;
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.chart.perfomance.dashboard.DashboardStackedBarChart#
	 * presentedSeriesData(javafx.scene.chart.XYChart.Series,
	 * javafx.scene.chart.XYChart.Series, javafx.scene.chart.XYChart.Series, int,
	 * java.lang.String)
	 */
	@Override
	protected void presentedSeriesData(Series<String, Number> series1, Series<String, Number> series2,
			Series<String, Number> series3, int i, String name) {
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
	@Override
	protected void presentedSeriesData(XYChart.Series<String, Number> series1, XYChart.Series<String, Number> series2,
			XYChart.Series<String, Number> series3, int i, String name, int count) {

		if (series1.getData().get(count) != null && series2.getData().get(count) != null
				&& series3.getData().get(count) != null) {
			series1.getData().get(count).setYValue(teamMembers.get(i).getOnTime());
			series2.getData().get(count).setYValue(teamMembers.get(i).getQuality());
			series3.getData().get(count).setYValue(teamMembers.get(i).getValueAdd());
		}

	}

}
