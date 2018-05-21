/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.app.chart.model.TeamMember;

import eu.hansolo.tilesfx.Tile;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

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
		return Double.valueOf(
				tm.getScore1() + tm.getScore2() + tm.getScore3() + tm.getValueAdd() + tm.getQuality() + tm.getOnTime())
				/ 3;
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

			// mouse clicked events
			data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				if (event.getButton().ordinal() == MouseButton.PRIMARY.ordinal()) {
					Bounds b1 = data.getNode().getBoundsInLocal();
					double newX = (b1.getWidth()) / 2 + b1.getMinX();
					double newY = (b1.getHeight()) / 2 + b1.getMinY();
					// Make sure pie wedge location is reset
					data.getNode().setTranslateX(0);
					data.getNode().setTranslateY(0);
					TranslateTransition tt = new TranslateTransition(Duration.millis(1500), data.getNode());
					tt.setByX(newX);
					tt.setByY(newY);
					tt.setAutoReverse(true);
					tt.setCycleCount(1);
					tt.play();
				}
			});

			data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				if (event.getButton().ordinal() == MouseButton.SECONDARY.ordinal()) {
					// Make sure pie wedge location is reset
					FadeTransition tt = new FadeTransition(Duration.millis(2500), data.getNode());
					// data.getNode().setTranslateX(0);
					// data.getNode().setTranslateY(0);
					tt.getNode().setTranslateX(0);
					tt.getNode().setTranslateY(0);
					// TODO currently it doesn't work ..Need to check on this.
					// tt.setAutoReverse(true);
					// tt.setCycleCount(1);
					// tt.play();
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

		Tile tile = generateCustomTile(chart, "Quarterly Trend Analysis", 450, 470);

		setAlignment(Pos.CENTER);

		getChildren().add(tile);

		// total score
		teamMembers.stream().forEach(tm -> {
			double score = Double.valueOf((tm.getScore1() + tm.getScore2() + tm.getScore3() + tm.getValueAdd()
					+ tm.getQuality() + tm.getOnTime())) / 3;
			totalScore += score;
		});
		System.out.println("Total Score : " + totalScore);

	}

	@Override
	public void startTimer(long now) {
		// TODO Auto-generated method stub

	}
}
