/**
 * 
 */
package com.app.chart.perfomance.dashboard;

import com.app.chart.model.SunburstBoundary;

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
	private final SunburstBoundary sunburstBoundary;

	/**
	 * @param spacing
	 */
	public DashboardSunburnChart(SunburstBoundary sunburstBoundary) {
		super(5);
		this.sunburstBoundary = sunburstBoundary;
		if (sunburstBoundary != null)
			initUI();
	}

	private void initUI() {
		TreeNode rootNode = new TreeNode(new ChartData(sunburstBoundary.getRootName()));
		sunburstBoundary.getSubBoundaries().stream().forEach(bs -> {
			TreeNode node = new TreeNode(new ChartData(bs.getFieldName(), bs.getScores(), bs.getColor().fecthColor()),
					rootNode);
			bs.getAttrBoundaries().stream().forEach(bsl -> {
				TreeNode subNode = new TreeNode(
						new ChartData(bsl.getFieldName(), bsl.getScores(), bsl.getColor().fecthColor()), node);
			});

		});
		sunburstTile = TileBuilder.create().skinType(SkinType.SUNBURST).prefSize(440, 490).title("").textVisible(false)
				.sunburstTree(rootNode).sunburstBackgroundColor(Tile.BACKGROUND).sunburstTextColor(Tile.BACKGROUND)
				.sunburstUseColorFromParent(true).sunburstTextOrientation(TextOrientation.ORTHOGONAL)
				.sunburstAutoTextColor(true).sunburstUseChartDataTextColor(true).sunburstInteractive(true).build();

		StackPane pane = new StackPane(sunburstTile);

		getChildren().add(generateCustomTile(pane, "Release History", 450, 500, "Releases & Features"));

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
		Tile tile = TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(width, height).title(title).textVisible(false).
		// TODO think of a name for this text .
				text(btmText).graphic(node).roundedCorners(true).build();
		return tile;
	}

	public boolean isSunburstChartDisplayable() {
		return sunburstBoundary == null ? Boolean.FALSE
				: sunburstBoundary.getRootName() == null ? Boolean.FALSE
						: sunburstBoundary.getSubBoundaries().size() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}

}
