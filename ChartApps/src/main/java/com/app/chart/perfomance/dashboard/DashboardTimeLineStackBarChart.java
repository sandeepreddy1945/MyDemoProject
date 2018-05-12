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
		this(teamMembers, "Month Wise Perfomance", "Scores Achieved", title);
	}

	public DashboardTimeLineStackBarChart(List<TeamMember> teamMembers, String categoryAxisName, String numberAxisName,
			String title) {
		super(teamMembers);
		this.teamMembers = teamMembers;
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
		series1.getData().add(new XYChart.Data<String, Number>(name, teamMembers.get(i).getOnTime()));
		series2.getData().add(new XYChart.Data<String, Number>(name, teamMembers.get(i).getQuality()));
		series3.getData().add(new XYChart.Data<String, Number>(name, teamMembers.get(i).getValueAdd()));
	}

}
