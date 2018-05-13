/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.app.chart.model.TeamMember;

import eu.hansolo.tilesfx.Tile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;

/**
 * @author Sandeep
 *
 */
public class DashboardPieChart extends DashboardAbstract {

	private PieChart chart;
	private double totalScore;

	/**
	 * @param spacing
	 */
	public DashboardPieChart(List<TeamMember> teamMembers) {
		super(teamMembers);
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

		chart.getData().stream().forEach(data -> {
			final Tooltip t = new Tooltip(data.getName() + " : "
					+ new DecimalFormat("#.##").format((data.getPieValue() / totalScore) * 100) + " %");
			data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					if (e.getSource() instanceof Node) {
						Node sender = (Node) e.getSource();
						Tooltip.install(sender, t);
						sender.setEffect(new Glow(0.5));
					}
				}
			});

			data.getNode().setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent me) {
					if (me.getSource() instanceof Node) {
						Node sender = (Node) me.getSource();
						sender.setEffect(null);
						t.hide();
					}
				}
			});
		});
	}

	@Override
	public void initUI() {
		ObservableList<Data> pieChartData = FXCollections.observableArrayList(createPieChartData());
		chart = new PieChart(pieChartData);
		chart.setTitle("Pie Chart");
		chart.setClockwise(true);
		chart.setLabelLineLength(20);
		chart.setLabelsVisible(true);
		chart.getStylesheets().add(getClass().getResource("teammeberstylesheet.css").toExternalForm());

		Tile tile = generateCustomTile(chart, "Pie Chart", 450, 500);

		setAlignment(Pos.CENTER);

		getChildren().add(tile);

		// total score
		teamMembers.stream().forEach(tm -> {
			double score = Double.valueOf((tm.getScore1() + tm.getScore2() + tm.getScore3())) / 3;
			totalScore += score;
		});
		System.out.println("Total Score : " + totalScore);

	}

	@Override
	public void startTimer(long now) {
		// TODO Auto-generated method stub

	}
}
