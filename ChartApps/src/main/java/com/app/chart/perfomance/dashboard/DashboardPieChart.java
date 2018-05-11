/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.util.ArrayList;
import java.util.List;

import com.app.chart.model.TeamMember;

import eu.hansolo.tilesfx.Tile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * @author Sandeep
 *
 */
public class DashboardPieChart extends DashboardAbstract {

	final List<TeamMember> teamMembers;
	private PieChart chart;

	/**
	 * @param spacing
	 */
	public DashboardPieChart(List<TeamMember> teamMembers) {
		super(teamMembers);
		this.teamMembers = teamMembers;
		// add listeners
		addListenersForNodes();
	}

	private List<PieChart.Data> createPieChartData() {
		List<PieChart.Data> datas = new ArrayList<>();
		teamMembers.stream().forEach(t -> {
			datas.add(new PieChart.Data(t.getName(), calculateScore(t)));
		});

		return datas;
	}

	private double calculateScore(TeamMember tm) {
		return Double.valueOf(tm.getScore1() + tm.getScore2() + tm.getScore3()) / 3;
	}

	private void addListenersForNodes() {
		final Label caption = new Label("");
		caption.setTextFill(Color.RED);
		caption.setStyle("-fx-font: 24 arial;");

		for (final PieChart.Data data : chart.getData()) {
			data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					caption.setTranslateX(e.getSceneX());
					caption.setTranslateY(e.getSceneY());
					caption.setText(String.valueOf(data.getPieValue()) + "%");
				}
			});
		}
	}

	@Override
	public void initUI() {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(createPieChartData());
		chart = new PieChart(pieChartData);
		chart.setAnimated(true);
		chart.setClockwise(false);
		chart.setLabelLineLength(0);
		// chart.setLabelsVisible(true);
		// chart.setEffect(getEffect());
		chart.setLegendVisible(true);
		chart.setLegendSide(Side.BOTTOM);

		Tile tile = generateCustomTile(chart);

		setAlignment(Pos.CENTER);

		getChildren().add(tile);

	}

	@Override
	public void startTimer(long now) {
		// TODO Auto-generated method stub

	}
}
