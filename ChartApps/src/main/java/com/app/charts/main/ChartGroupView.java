/**
 * 
 */
package com.app.charts.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.app.chart.fx.FilesUtil;
import com.app.chart.model.RunJsonBoundary;
import com.app.chart.perfomance.dashboard.DashboardUtil;
import com.app.chart.run.ui.DisplayBoardConstants;
import com.jfoenix.controls.JFXScrollPane;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Tile.SkinType;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author Sandeep Reddy Battula
 *
 */
public class ChartGroupView extends HBox {

	private List<RunJsonBoundary> runJsonBoundaries = new ArrayList<>();
	private Timeline timeline;

	private int colCount = 0;
	private int rowCount = 0;

	/**
	 * @param spacing
	 */
	public ChartGroupView(List<RunJsonBoundary> runJsonBoundaries, Timeline timeline) {
		super(10);
		this.runJsonBoundaries = runJsonBoundaries;
		this.timeline = timeline;
		initUI();

	}

	private void initUI() {
		HBox ruleBox = new HBox(10);
		VBox mainBox = new VBox(20);
		JFXScrollPane jfxScrollPane = new JFXScrollPane();
		jfxScrollPane.getChildren().add(mainBox);
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10));
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		HBox header = DashboardUtil.HeaderSegment(ruleBox, "MPS Organizational Entities View");
		mainBox.getChildren().add(header);

		// currently intended to display 5 per row and 5 columns per page in total 25.

		if (runJsonBoundaries != null && runJsonBoundaries.size() > 0) {
			runJsonBoundaries.stream().forEach(r -> {
				if (colCount == 5) {
					colCount = 0;
					rowCount++;
				}
				if (DisplayBoardConstants.image.name().equals(r.getType())) {
					ImageView imageView = null;
					try {
						imageView = new ImageView(new Image(FileUtils
								.openInputStream(new File(FilesUtil.IMAGES_DIR_PATH + FilesUtil.SLASH + r.getPath()))));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					gridPane.add(buildTileFromData(r.getType(), r.getPath(), imageView), colCount++, rowCount);
				} else if (DisplayBoardConstants.chart.name().equals(r.getType())) {
					gridPane.add(buildTextTileFromData(r.getType(), r.getPath(), r.getDisplayTxt()), colCount++,
							rowCount);
				} else if (DisplayBoardConstants.dashboard.name().equals(r.getType())) {
					gridPane.add(buildTextTileFromData(r.getType(), r.getPath(), "MPS Dashboard Views"), colCount++,
							rowCount);
				} else if (DisplayBoardConstants.customer.name().equals(r.getType())) {
					// for now not implemented.
				}
			});
		}

		mainBox.getChildren().add(gridPane);
		HBox footer = DashboardUtil.FooterSegment();
		// mainBox.getChildren().add(footer);

		ruleBox.getChildren().add(mainBox);

		getChildren().add(ruleBox);

	}

	private Tile buildTileFromData(String tile, String text, ImageView imageView) {

		Tile dataTile = TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(250, 250).title(tile).text(text)
				.graphic(imageView).roundedCorners(true).build();

		return dataTile;
	}

	private Tile buildTextTileFromData(String title, String text, String description) {
		Tile textTile = TileBuilder.create().skinType(SkinType.TEXT).prefSize(250, 250).title(title).text(text)
				.description(description).descriptionAlignment(Pos.CENTER).textVisible(true).build();

		return textTile;
	}

}
