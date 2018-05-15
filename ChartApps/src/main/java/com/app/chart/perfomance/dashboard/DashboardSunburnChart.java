/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.chart.SunburstChart.TextOrientation;
import eu.hansolo.tilesfx.tools.TreeNode;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * @author Sandeep
 *
 */
public class DashboardSunburnChart extends HBox {

	private Tile sunburstTile;

	/**
	 * @param spacing
	 */
	public DashboardSunburnChart() {
		super(5);
		initUI();
	}

	private void initUI() {
		TreeNode tree = new TreeNode(new ChartData("ROOT"));
		TreeNode first = new TreeNode(new ChartData("1st", 8.3, Tile.BLUE), tree);
		TreeNode second = new TreeNode(new ChartData("2nd", 2.2, Tile.ORANGE), tree);
		TreeNode third = new TreeNode(new ChartData("3rd", 1.4, Tile.PINK), tree);
		TreeNode fourth = new TreeNode(new ChartData("4th", 1.2, Tile.LIGHT_GREEN), tree);

		TreeNode jan = new TreeNode(new ChartData("Jan", 3.5), first);
		TreeNode feb = new TreeNode(new ChartData("Feb", 3.1), first);
		TreeNode mar = new TreeNode(new ChartData("Mar", 1.7), first);
		TreeNode apr = new TreeNode(new ChartData("Apr", 1.1), second);
		TreeNode may = new TreeNode(new ChartData("May", 0.8), second);
		TreeNode jun = new TreeNode(new ChartData("Jun", 0.3), second);
		TreeNode jul = new TreeNode(new ChartData("Jul", 0.7), third);
		TreeNode aug = new TreeNode(new ChartData("Aug", 0.6), third);
		TreeNode sep = new TreeNode(new ChartData("Sep", 0.1), third);
		TreeNode oct = new TreeNode(new ChartData("Oct", 0.5), fourth);
		TreeNode nov = new TreeNode(new ChartData("Nov", 0.4), fourth);
		TreeNode dec = new TreeNode(new ChartData("Dec", 0.3), fourth);

		sunburstTile = TileBuilder.create().skinType(SkinType.SUNBURST).prefSize(440, 490).title("").textVisible(false)
				.sunburstTree(tree).sunburstBackgroundColor(Tile.BACKGROUND).sunburstTextColor(Tile.BACKGROUND)
				.sunburstUseColorFromParent(true).sunburstTextOrientation(TextOrientation.ORTHOGONAL)
				.sunburstAutoTextColor(true).sunburstUseChartDataTextColor(true).sunburstInteractive(true).build();

		StackPane pane = new StackPane(sunburstTile);

		getChildren().add(generateCustomTile(pane, "Release Highlights", 450, 500, ""));

	}

	/**
	 * 
	 * @param node
	 * @param title
	 * @param width
	 * @param height
	 * @param btmText
	 * @return
	 */
	public Tile generateCustomTile(Node node, String title, double width, double height, String btmText) {
		Tile tile = TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(width, height).title(title).
		// TODO think of a name for this text .
				text(btmText).graphic(node).roundedCorners(true).build();
		return tile;
	}

}
